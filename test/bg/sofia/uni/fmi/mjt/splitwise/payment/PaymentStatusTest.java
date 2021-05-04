package bg.sofia.uni.fmi.mjt.splitwise.payment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PaymentStatusTest {
    private PaymentStatus status;

    @Before
    public void setUp() {
        status = new PaymentStatus();
    }

    @Test
    public void getType() {
        assertEquals("In the beginning should be even",
                PaymentStatusType.EVEN, status.getType());
    }

    @Test
    public void getAmount() {
        status.getMoney(5.26);
        assertEquals(5.26, status.getAmount(), 0.001);
    }

    @Test
    public void addMoney() {
        status.addMoney(15.5);
        assertEquals(PaymentStatusType.SOMEBODY_OWES, status.getType());
    }

    @Test
    public void getMoney() {
        status.getMoney(15.5);
        assertEquals(PaymentStatusType.YOU_OWE, status.getType());
    }

    @Test
    public void clearStatus() {
        status.getMoney(5);
        status.clearStatus();
        assertTrue(status.isEven());
    }

    @Test
    public void isEven() {
        status.addMoney(5);
        status.getMoney(5);
        assertTrue(status.isEven());
        status.addMoney(0.5);
        assertFalse(status.isEven());
    }
}