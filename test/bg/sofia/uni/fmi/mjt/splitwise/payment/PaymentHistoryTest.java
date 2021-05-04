package bg.sofia.uni.fmi.mjt.splitwise.payment;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static bg.sofia.uni.fmi.mjt.splitwise.payment.PaymentHistory.NO_PAYMENTS_MESSAGE;
import static org.junit.Assert.*;

public class PaymentHistoryTest {
    private PaymentHistory payments;
    private final LocalDateTime testTime1 = LocalDateTime.of(2021, 1, 10, 16, 18, 33);
    private final LocalDateTime testTime2 = LocalDateTime.of(2021, 1, 10, 18, 18, 33);
    private final LocalDateTime testTime3 = LocalDateTime.of(2021, 1, 12, 5, 18, 33);

    @Before
    public void setUp() {
        payments = new PaymentHistory();
    }

    @Test
    public void getPaymentsIfNoPayments() {
        assertEquals("There should be no payments",
                NO_PAYMENTS_MESSAGE, payments.getPayments());
    }

    @Test
    public void getPaymentsWithUser() {
        payments.addPayment(testTime1, "I pay Eric(eric) 18 lv");
        payments.addPayment(testTime2, "I pay Eric(eric) 4 lv");
        payments.addPayment(testTime2, "I pay Bobi(bob) 4 lv");

        String expected = "[10.01.2021 16:18:33]: I pay Eric(eric) 18 lv" + System.lineSeparator()
                + "[10.01.2021 18:18:33]: I pay Eric(eric) 4 lv";
        assertEquals("Should be 2 payments", expected, payments.getPaymentsWithName("eric"));

        assertEquals("There are no payments with gogo",
                NO_PAYMENTS_MESSAGE, payments.getPaymentsWithName("gogo"));

        expected = "[10.01.2021 18:18:33]: I pay Bobi(bob) 4 lv";
        assertEquals("There should be one payment with bob",
                expected,payments.getPaymentsWithName("bob"));
    }

    @Test
    public void getPaymentsBefore() {
        payments.addPayment(testTime1, "I pay Eric(eric) 18 lv");
        payments.addPayment(testTime2, "I pay John(jhn) 4 lv");
        payments.addPayment(testTime3, "I pay Carl(car) 7 lv");

        String expected = "[10.01.2021 16:18:33]: I pay Eric(eric) 18 lv" + System.lineSeparator()
                + "[10.01.2021 18:18:33]: I pay John(jhn) 4 lv";
        assertEquals("Should be first 2 payments",
                expected, payments.getPaymentsBefore(testTime3));

        assertEquals("There should be no payments",
                NO_PAYMENTS_MESSAGE, payments.getPaymentsBefore(testTime1));
    }

    @Test
    public void getPaymentsAfter() {
        payments.addPayment(testTime1, "I pay Eric(eric) 18 lv");
        payments.addPayment(testTime2, "I pay John(jhn) 4 lv");
        payments.addPayment(testTime3, "I pay Carl(car) 7 lv");

        String expected = "[10.01.2021 18:18:33]: I pay John(jhn) 4 lv" + System.lineSeparator()
                + "[12.01.2021 05:18:33]: I pay Carl(car) 7 lv";

        assertEquals("Should be second and third payments",
                expected, payments.getPaymentsAfter(testTime1));

        assertEquals("There should be no payments",
                NO_PAYMENTS_MESSAGE, payments.getPaymentsAfter(testTime3));
    }

    @Test
    public void addPayment() {
        payments.addPayment(testTime1, "I pay Eric(eric) 18 lv");
        String expected = "[10.01.2021 16:18:33]: I pay Eric(eric) 18 lv";
        assertEquals("Should be added one payment",
                expected, payments.getPayments());
    }
}