package bg.sofia.uni.fmi.mjt.splitwise.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateToStringParser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static String parse(LocalDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Null dateTime");
        }
        return dateTime.format(formatter);
    }
}
