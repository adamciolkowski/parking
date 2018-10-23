package pl.touk.parking.payment;

import pl.touk.parking.domain.Money;

import java.math.BigDecimal;

public class RegularDriverPaymentPolicy extends AbstractPaymentPolicy {

    @Override
    protected Money firstHourPayment() {
        return Money.of(BigDecimal.ONE);
    }

    @Override
    protected double thirdAndEachNextHourMultiplier() {
        return 1.5;
    }
}
