package pl.touk.parking;

import java.time.*;

public class TimeUtil {

    public static Clock at(LocalTime time) {
        return at(LocalDate.of(2018, Month.JULY, 17).atTime(time));
    }

    public static Clock at(LocalDateTime time) {
        return Clock.fixed(
                time.toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
    }
}
