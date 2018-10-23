package pl.touk.parking.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.touk.parking.ParkingException;
import pl.touk.parking.domain.Money;
import pl.touk.parking.web.security.SecurityRoles;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
public class ParkingOwnerController {

    private final ParkingOwnerService ownerService;

    @Autowired
    public ParkingOwnerController(ParkingOwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("income")
    @RolesAllowed(SecurityRoles.OWNER)
    public BigDecimal calculateIncome(@RequestParam String date) {
        LocalDate parse = parseDate(date);
        Money payment = ownerService.calculateIncomeFor(parse);
        return payment.toBigDecimal();
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new ParkingException(e.getMessage(), e);
        }
    }
}
