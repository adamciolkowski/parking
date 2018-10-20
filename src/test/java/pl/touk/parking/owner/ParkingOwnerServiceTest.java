package pl.touk.parking.owner;

import org.junit.Before;
import org.junit.Test;
import pl.touk.parking.InMemoryParkingUsageRepository;
import pl.touk.parking.repository.ParkingUsageRepository;
import pl.touk.parking.PaymentPolicy;
import pl.touk.parking.domain.Driver;
import pl.touk.parking.domain.Money;
import pl.touk.parking.usage.ParkingUsageService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.time.Month.JULY;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.parking.TimeUtil.at;

public class ParkingOwnerServiceTest {

    ParkingUsageService parkingUsageService;
    ParkingOwnerService parkingOwnerService;

    @Before
    public void setUp() {
        ParkingUsageRepository repository = new InMemoryParkingUsageRepository();
        parkingUsageService = new ParkingUsageService(repository);
        parkingOwnerService = new ParkingOwnerService(repository);
    }

    @Test
    public void unusedParkingGeneratesZeroIncome() {
        Money money = parkingOwnerService.calculateIncomeFor(LocalDate.of(2018, JULY, 17));

        assertThat(money).isEqualTo(Money.zero());
    }

    @Test
    public void returnsSumAllOfPaymentsMadeOnGivenDay() {
        useParking(Money.of(BigDecimal.valueOf(1)), LocalDate.of(2018, JULY, 16).atTime(23, 59, 59));
        useParking(Money.of(BigDecimal.valueOf(2.50)), LocalDate.of(2018, JULY, 17).atStartOfDay());
        useParking(Money.of(BigDecimal.valueOf(5)), LocalDate.of(2018, JULY, 17).atTime(23, 59, 59));
        useParking(Money.of(BigDecimal.valueOf(11)), LocalDate.of(2018, JULY, 18).atStartOfDay());

        Money money = parkingOwnerService.calculateIncomeFor(LocalDate.of(2018, JULY, 17));

        assertThat(money).isEqualTo(Money.of(BigDecimal.valueOf(7.50)));
    }

    @Test
    public void doesNotIncludeUnfinishedParkingUsages() {
        startParkingMeter(Money.of(BigDecimal.valueOf(10)));
        useParking(Money.of(BigDecimal.valueOf(2)));

        Money money = parkingOwnerService.calculateIncomeFor(LocalDate.of(2018, JULY, 17));

        assertThat(money).isEqualTo(Money.of(BigDecimal.valueOf(2)));
    }

    @Test
    public void shouldSumConsecutiveParkingUsagesByTheSameVehicle() {
        useParking(Money.of(BigDecimal.valueOf(7)), LocalTime.of(16, 1));
        useParking(Money.of(BigDecimal.valueOf(9)), LocalTime.of(16, 21));

        Money money = parkingOwnerService.calculateIncomeFor(LocalDate.of(2018, JULY, 17));

        assertThat(money).isEqualTo(Money.of(BigDecimal.valueOf(16)));
    }

    private void useParking(Money payment) {
        useParking(payment, LocalTime.of(16, 1));
    }

    private void startParkingMeter(Money futurePayment) {
        Driver driver = new Driver("KR00002", new FixedPaymentPolicy(futurePayment));
        parkingUsageService.startParkingMeter(driver, at(LocalTime.of(12, 0)));
    }

    private void useParking(Money payment, LocalTime endTime) {
        useParking(payment, LocalDate.of(2018, JULY, 17).atTime(endTime));
    }

    private void useParking(Money payment, LocalDateTime endTime) {
        Driver driver = new Driver("KR00001", new FixedPaymentPolicy(payment));
        parkingUsageService.startParkingMeter(driver, at(endTime.minusHours(1)));
        parkingUsageService.stopParkingMeter(driver.getVehicleNumber(), at(endTime));
    }

    static class FixedPaymentPolicy implements PaymentPolicy {

        private final Money amount;

        FixedPaymentPolicy(Money amount) {
            this.amount = amount;
        }

        @Override
        public Money calculatePayment(int hours) {
            return amount;
        }
    }
}
