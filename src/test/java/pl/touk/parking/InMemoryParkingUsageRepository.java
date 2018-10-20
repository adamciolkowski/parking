package pl.touk.parking;

import pl.touk.parking.domain.ParkingUsage;
import pl.touk.parking.repository.ParkingUsageRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class InMemoryParkingUsageRepository implements ParkingUsageRepository {

    private final IdGenerator idGenerator = new IdGenerator();

    private final Map<Long, ParkingUsage> usages = new HashMap<>();

    @Override
    public void save(ParkingUsage usage) {
        if (usage.getId() == null)
            usage.setId(idGenerator.nextId());
        usages.put(usage.getId(), usage);
    }

    @Override
    public Optional<ParkingUsage> findByDriverVehicleNumberAndEndTimeIsNull(String vehicleNumber) {
        for (ParkingUsage usage : usages.values()) {
            if (!usage.hasEnded() && usage.isByDriverOfVehicleWith(vehicleNumber))
                return Optional.of(usage);
        }
        return Optional.empty();
    }

    @Override
    public Collection<ParkingUsage> findAllByStartTimeGreaterThanEqualAndEndTimeBefore(
            LocalDateTime start, LocalDateTime end) {
        return usages.values().stream()
                .filter(usage -> usage.endedBetween(start, end))
                .collect(toList());
    }
}
