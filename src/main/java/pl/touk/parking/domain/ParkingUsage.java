package pl.touk.parking.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;
import static javax.persistence.CascadeType.PERSIST;

@Entity
public class ParkingUsage {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = PERSIST)
    private Driver driver;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Money payment;

    ParkingUsage() { }

    public ParkingUsage(Driver driver, Clock clock) {
        this.driver = driver;
        this.startTime = LocalDateTime.now(clock);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void finish(Clock clock) {
        endTime = LocalDateTime.now(clock);
        payment = calculatePayment();
    }

    public boolean hasEnded() {
        return endTime != null;
    }

    public boolean endedBetween(LocalDateTime startInclusive, LocalDateTime end) {
        return hasEnded() && endTimeIsBetween(startInclusive, end);
    }

    public boolean isByDriverOfVehicleWith(String vehicleNumber) {
        return driver.usesVehicleWith(vehicleNumber);
    }

    public Money getPayment() {
        return payment;
    }

    private Money calculatePayment() {
        long hours = HOURS.between(startTime, endTime) + 1;
        return driver.calculatePayment((int) hours);
    }

    private boolean endTimeIsBetween(LocalDateTime startInclusive, LocalDateTime end) {
        return endTime.compareTo(startInclusive) >= 0 && endTime.isBefore(end);
    }
}
