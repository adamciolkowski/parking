package pl.touk.parking.usage;

import org.junit.Before;
import org.junit.Test;
import pl.touk.parking.InMemoryParkingUsageRepository;
import pl.touk.parking.ParkingException;
import pl.touk.parking.domain.Driver;
import pl.touk.parking.payment.RegularDriverPaymentPolicy;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.touk.parking.TimeUtil.at;

public class ParkingUsageServiceTest {

    ParkingUsageService service;

    @Before
    public void setUp() {
        service = new ParkingUsageService(new InMemoryParkingUsageRepository());
    }

    @Test
    public void throwsExceptionWhenDriverAttemptsToStartRunningParkingMeter() {
        service.startParkingMeter(driver(), at(LocalTime.of(16, 10)));

        assertThatThrownBy(() -> service.startParkingMeter(driver(), at(LocalTime.of(16, 20))))
                .isInstanceOf(ParkingException.class)
                .hasMessage("Parking meter already started");
    }

    @Test
    public void throwsExceptionWhenDriverAttemptsToStopNotRunningParkingMeter() {
        assertThatThrownBy(() -> service.stopParkingMeter(driver().getVehicleNumber(), at(LocalTime.of(16, 20))))
                .isInstanceOf(ParkingException.class)
                .hasMessage("Parking meter not started");
    }

    private Driver driver() {
        return new Driver("KR12345", new RegularDriverPaymentPolicy());
    }
}
