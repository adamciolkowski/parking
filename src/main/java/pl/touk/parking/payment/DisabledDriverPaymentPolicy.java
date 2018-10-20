package pl.touk.parking.payment;

import pl.touk.parking.domain.Money;

public class DisabledDriverPaymentPolicy extends AbstractPaymentPolicy {

    @Override
    protected Money firstHourPayment() {
        return Money.zero();
    }

    @Override
    protected double thirdAndEachNextHourMultiplier() {
        return 1.2;
    }
}
