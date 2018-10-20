package pl.touk.parking.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.touk.parking.repository.ParkingUsageRepository;

@Service
public class ParkingOperatorService {

    private final ParkingUsageRepository repository;

    @Autowired
    public ParkingOperatorService(ParkingUsageRepository repository) {
        this.repository = repository;
    }

    public boolean hasStartedParkingMeter(String vehicleNumber) {
        return repository.findByDriverVehicleNumberAndEndTimeIsNull(vehicleNumber).isPresent();
    }
}
