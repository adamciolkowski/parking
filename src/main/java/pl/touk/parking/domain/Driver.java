package pl.touk.parking.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;
import pl.touk.parking.PaymentPolicy;

import javax.persistence.Entity;
import java.time.Clock;

@Entity
public class Driver extends AbstractPersistable<Integer> {

    private String vehicleNumber;

    private PaymentPolicy paymentPolicy;

    Driver() { }

    public Driver(String vehicleNumber, PaymentPolicy paymentPolicy) {
        this.vehicleNumber = vehicleNumber;
        this.paymentPolicy = paymentPolicy;
    }

    public ParkingUsage startParkingMeter(Clock clock) {
        return new ParkingUsage(this, clock);
    }

    public Money calculatePayment(int hours) {
        return paymentPolicy.calculatePayment(hours);
    }

    public boolean usesVehicleWith(String vehicleNumber) {
        return this.vehicleNumber.equals(vehicleNumber);
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return vehicleNumber.equals(driver.vehicleNumber);
    }

    @Override
    public int hashCode() {
        return vehicleNumber.hashCode();
    }

    @Override
    public String toString() {
        return "Driver{" + vehicleNumber + "', " + paymentPolicy.getClass().getSimpleName() + '}';
    }
}
