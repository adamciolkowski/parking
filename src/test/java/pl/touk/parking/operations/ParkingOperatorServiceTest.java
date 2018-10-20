package pl.touk.parking.operations;

import org.junit.Before;
import org.junit.Test;
import pl.touk.parking.InMemoryParkingUsageRepository;
import pl.touk.parking.repository.ParkingUsageRepository;
import pl.touk.parking.domain.Driver;
import pl.touk.parking.payment.RegularDriverPaymentPolicy;
import pl.touk.parking.usage.ParkingUsageService;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.touk.parking.TimeUtil.at;

public class ParkingOperatorServiceTest {

    ParkingUsageService parkingUsageService;
    ParkingOperatorService operatorService;

    @Before
    public void setUp() {
        ParkingUsageRepository repository = new InMemoryParkingUsageRepository();
        operatorService = new ParkingOperatorService(repository);
        parkingUsageService = new ParkingUsageService(repository);
    }

    @Test
    public void returnsFalseIfVehicleHasNotStartedParkingMeter() {
        assertThat(operatorService.hasStartedParkingMeter(vehicleNumber())).isFalse();
    }

    @Test
    public void returnsTrueIfVehicleHasStartedParkingMeter() {
        parkingUsageService.startParkingMeter(driverOfVehicleWith(vehicleNumber()), at(LocalTime.of(16, 0)));

        assertThat(operatorService.hasStartedParkingMeter(vehicleNumber())).isTrue();
        assertThat(operatorService.hasStartedParkingMeter(otherVehicleNumber())).isFalse();
    }

    @Test
    public void returnsFalseIfVehicleHasStartedAndThenStoppedParkingMeter() {
        parkingUsageService.startParkingMeter(driverOfVehicleWith(vehicleNumber()), at(LocalTime.of(16, 0)));
        parkingUsageService.stopParkingMeter(driverOfVehicleWith(vehicleNumber()).getVehicleNumber(), at(LocalTime.of(16, 10)));

        assertThat(operatorService.hasStartedParkingMeter(vehicleNumber())).isFalse();
    }

    private String vehicleNumber() {
        return "KR12345";
    }

    private String otherVehicleNumber() {
        return "KR00000";
    }

    private Driver driverOfVehicleWith(String vehicleNumber) {
        return new Driver(vehicleNumber, new RegularDriverPaymentPolicy());
    }
}
