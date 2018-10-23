package pl.touk.parking.repository.converter;

import pl.touk.parking.PaymentPolicy;
import pl.touk.parking.payment.DisabledDriverPaymentPolicy;
import pl.touk.parking.payment.RegularDriverPaymentPolicy;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class PaymentPolicyConverter implements AttributeConverter<PaymentPolicy, String> {

    private static final String REGULAR = "REGULAR";
    private static final String DISABLED = "DISABLED";

    @Override
    public String convertToDatabaseColumn(PaymentPolicy policy) {
        if (policy instanceof RegularDriverPaymentPolicy)
            return REGULAR;
        if (policy instanceof DisabledDriverPaymentPolicy)
            return DISABLED;
        throw new IllegalArgumentException("Unknown policy: " + policy);
    }

    @Override
    public PaymentPolicy convertToEntityAttribute(String dbData) {
        if (dbData.equals(REGULAR))
            return new RegularDriverPaymentPolicy();
        if (dbData.equals(DISABLED))
            return new DisabledDriverPaymentPolicy();
        throw new IllegalArgumentException("Unknown policy: " + dbData);
    }
}
