package pl.touk.parking.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.touk.parking.web.security.SecurityRoles;

import javax.annotation.security.RolesAllowed;

@RestController
public class ParkingOperatorController {

    private final ParkingOperatorService operatorService;

    @Autowired
    public ParkingOperatorController(ParkingOperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("check")
    @RolesAllowed(SecurityRoles.OPERATOR)
    public Boolean checkParkingMeterStarted(@RequestParam String vehicleNumber) {
        return operatorService.hasStartedParkingMeter(vehicleNumber);
    }
}
