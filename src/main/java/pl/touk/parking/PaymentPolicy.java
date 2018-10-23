package pl.touk.parking;

import pl.touk.parking.domain.Money;

public interface PaymentPolicy {

    Money calculatePayment(int hours);
}
