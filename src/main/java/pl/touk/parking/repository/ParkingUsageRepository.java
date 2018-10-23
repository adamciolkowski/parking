package pl.touk.parking.repository;

import org.springframework.data.repository.Repository;
import pl.touk.parking.domain.ParkingUsage;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface ParkingUsageRepository extends Repository<ParkingUsage, Long> {

    void save(ParkingUsage usage);

    Optional<ParkingUsage> findByDriverVehicleNumberAndEndTimeIsNull(String vehicleNumber);

    Collection<ParkingUsage> findAllByStartTimeGreaterThanEqualAndEndTimeBefore(LocalDateTime start, LocalDateTime end);
}
