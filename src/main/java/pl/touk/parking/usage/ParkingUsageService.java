package pl.touk.parking.usage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.touk.parking.ParkingException;
import pl.touk.parking.repository.ParkingUsageRepository;
import pl.touk.parking.domain.Driver;
import pl.touk.parking.domain.Money;
import pl.touk.parking.domain.ParkingUsage;

import java.time.Clock;

@Service
public class ParkingUsageService {

    private final ParkingUsageRepository repository;

    @Autowired
    public ParkingUsageService(ParkingUsageRepository repository) {
        this.repository = repository;
    }

    public void startParkingMeter(Driver driver, Clock clock) {
        checkNotStarted(driver);
        ParkingUsage usage = driver.startParkingMeter(clock);
        repository.save(usage);
    }

    public Money stopParkingMeter(String vehicleNumber, Clock clock) {
        ParkingUsage usage = repository.findByDriverVehicleNumberAndEndTimeIsNull(vehicleNumber)
                .orElseThrow(() -> new ParkingException("Parking meter not started"));
        usage.finish(clock);
        repository.save(usage);
        return usage.getPayment();
    }

    private void checkNotStarted(Driver driver) {
        if (repository.findByDriverVehicleNumberAndEndTimeIsNull(driver.getVehicleNumber()).isPresent())
            throw new ParkingException("Parking meter already started");
    }
}
