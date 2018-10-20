package pl.touk.parking.usage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.touk.parking.domain.Driver;
import pl.touk.parking.domain.Money;
import pl.touk.parking.web.DriverDto;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Clock;

@RestController
public class ParkingController {

    private final ParkingUsageService usageService;

    private final Clock clock = Clock.systemDefaultZone();

    @Autowired
    public ParkingController(ParkingUsageService usageService) {
        this.usageService = usageService;
    }

    @GetMapping("start")
    public String startParkingMeter(@Valid DriverDto dto) {
        Driver driver = dto.create();
        usageService.startParkingMeter(driver, clock);
        return "ok";
    }

    @GetMapping("stop")
    public BigDecimal stopParkingMeter(@RequestParam String vehicleNumber) {
        Money payment = usageService.stopParkingMeter(vehicleNumber, clock);
        return payment.toBigDecimal();
    }
}
