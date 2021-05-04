package bg.sofia.uni.fmi.mjt.splitwise.payment;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentHistory {
    private final List<PaymentHolder> payments;
    public static final String NO_PAYMENTS_MESSAGE = "There are no payments.";
//    public static final String PAID_FRIEND = "You paid %.2f LV to %s";
//    public static final String PAID_USER_IN_GROUP = "You paid %.2f LV to %s in group '%s'";
//    public static final String SPLIT_FRIEND = "You split %.2f LV with %s for [%s]";
//    public static final String SPLIT_GROUP = "You split %.2f LV in group '%s' for [%s]";

    public PaymentHistory() {
        this.payments = new LinkedList<>();
    }

    public String getPayments() {
        if (payments.isEmpty()) {
            return NO_PAYMENTS_MESSAGE;
        }
        return payments.stream()
                .map(PaymentHolder::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String getPaymentsWithName(String name) {
        if (name == null) {
            return NO_PAYMENTS_MESSAGE;
        }
        String result = payments.stream()
                .map(PaymentHolder::toString)
                .filter(p -> p.contains(name))
                .collect(Collectors.joining(System.lineSeparator()));
        if (result.isBlank()) {
            return NO_PAYMENTS_MESSAGE;
        }
        return result;
    }

    public String getPaymentsBefore(LocalDateTime dateTime) {
        String result = payments.stream()
                .filter(p -> p.getDateTime().isBefore(dateTime))
                .map(PaymentHolder::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        if(result.isBlank()){
            return NO_PAYMENTS_MESSAGE;
        }
        return result;
    }

    public String getPaymentsAfter(LocalDateTime dateTime) {
        String result = payments.stream()
                .filter(p -> p.getDateTime().isAfter(dateTime))
                .map(PaymentHolder::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        if(result.isBlank()){
            return NO_PAYMENTS_MESSAGE;
        }
        return result;
    }

    public void addPayment(LocalDateTime dateTime, String paymentDescription) {
        payments.add(new PaymentHolder(dateTime, paymentDescription));
    }
}
