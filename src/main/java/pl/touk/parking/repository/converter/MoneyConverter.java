package pl.touk.parking.repository.converter;

import pl.touk.parking.domain.Money;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Money attribute) {
        if (attribute == null)
            return null;
        return attribute.toBigDecimal();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal dbData) {
        if (dbData == null)
            return null;
        return Money.of(dbData);
    }
}
