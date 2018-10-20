package pl.touk.parking.web;

import pl.touk.parking.PaymentPolicy;
import pl.touk.parking.payment.DisabledDriverPaymentPolicy;
import pl.touk.parking.payment.RegularDriverPaymentPolicy;

public enum DriverType {
    REGULAR(new RegularDriverPaymentPolicy()),
    DISABLED(new DisabledDriverPaymentPolicy());

    private final PaymentPolicy paymentPolicy;

    DriverType(PaymentPolicy paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public PaymentPolicy getPaymentPolicy() {
        return paymentPolicy;
    }
}
