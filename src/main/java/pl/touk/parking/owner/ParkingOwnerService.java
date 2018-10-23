package pl.touk.parking.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.touk.parking.repository.ParkingUsageRepository;
import pl.touk.parking.domain.Money;
import pl.touk.parking.domain.ParkingUsage;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ParkingOwnerService {

    private final ParkingUsageRepository repository;

    @Autowired
    public ParkingOwnerService(ParkingUsageRepository repository) {
        this.repository = repository;
    }

    public Money calculateIncomeFor(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return calculateIncomeBetween(start, end);
    }

    private Money calculateIncomeBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findAllByStartTimeGreaterThanEqualAndEndTimeBefore(start, end).stream()
                .map(ParkingUsage::getPayment)
                .reduce(Money.zero(), Money::add);
    }
}
