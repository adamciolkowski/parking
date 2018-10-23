package pl.touk.parking.payment;

import pl.touk.parking.PaymentPolicy;
import pl.touk.parking.domain.Money;

import java.math.BigDecimal;

public abstract class AbstractPaymentPolicy implements PaymentPolicy {

    @Override
    public Money calculatePayment(int hours) {
        if (hours == 1)
            return firstHourPayment();
        if (hours == 2)
            return secondHourPayment();
        return secondHourPayment().multiply(multiplier(hours));
    }

    private Money secondHourPayment() {
        return Money.of(BigDecimal.valueOf(2));
    }

    private BigDecimal multiplier(int hours) {
        return BigDecimal.valueOf(thirdAndEachNextHourMultiplier()).pow(hours - 2);
    }

    protected abstract Money firstHourPayment();

    protected abstract double thirdAndEachNextHourMultiplier();
}
