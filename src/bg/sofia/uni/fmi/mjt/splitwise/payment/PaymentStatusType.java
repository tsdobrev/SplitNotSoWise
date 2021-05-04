package bg.sofia.uni.fmi.mjt.splitwise.payment;

public enum PaymentStatusType {
    YOU_OWE, // when current user owes money to another user (I OWE somebody)
    SOMEBODY_OWES, // when other user owes money to current one (somebody OWES me)
    EVEN // when no one owes money
}
