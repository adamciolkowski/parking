package pl.touk.parking.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.UP));
    }

    public static Money zero() {
        return Money.of(BigDecimal.ZERO);
    }

    public Money add(Money money) {
        return Money.of(amount.add(money.amount));
    }

    public Money multiply(BigDecimal multiplicand) {
        return of(amount.multiply(multiplicand));
    }

    public BigDecimal toBigDecimal() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
