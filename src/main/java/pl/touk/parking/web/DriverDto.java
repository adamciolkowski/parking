package pl.touk.parking.web;

import pl.touk.parking.domain.Driver;

import javax.validation.constraints.NotNull;

public class DriverDto {

    @NotNull(message = "vehicleNumber is required")
    String vehicleNumber;

    @NotNull(message = "driverType is required")
    DriverType driverType;

    public Driver create() {
        return new Driver(vehicleNumber, driverType.getPaymentPolicy());
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setDriverType(DriverType driverType) {
        this.driverType = driverType;
    }
}
