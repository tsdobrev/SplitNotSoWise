package bg.sofia.uni.fmi.mjt.splitwise.payment;

import bg.sofia.uni.fmi.mjt.splitwise.utilities.DateToStringParser;

import java.time.LocalDateTime;
import java.util.Objects;

public class PaymentHolder {
    private final LocalDateTime dateTime;
    private final String description;

    public PaymentHolder(LocalDateTime dateTime, String description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PaymentHolder that = (PaymentHolder) object;
        return dateTime.equals(that.dateTime) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, description);
    }

    @Override
    public String toString() {
        return String.format("[%s]: %s", DateToStringParser.parse(dateTime), description);
    }
}
