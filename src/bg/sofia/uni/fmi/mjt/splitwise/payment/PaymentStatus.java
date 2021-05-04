package bg.sofia.uni.fmi.mjt.splitwise.payment;

public class PaymentStatus {
    private PaymentStatusType type;
    private double amount;

    public PaymentStatus() {
        clearStatus();
    }

    public PaymentStatusType getType() {
        return type;
    }

    public double getAmount() {
        return Math.abs(amount);
    }

    public void addMoney(double amount) { // when you pay
        amount = Math.max(0, amount);
        this.amount += amount;
        checkStatusType();
    }

    public void getMoney(double amount) { // when others pay for something
        amount = Math.max(0, amount);
        this.amount -= amount;
        checkStatusType();
    }

    public void clearStatus() {
        type = PaymentStatusType.EVEN;
        amount = 0;
    }

    public boolean isEven() {
        return type == PaymentStatusType.EVEN;
    }

    // if amount is negative you owe to someone; if positive - someone owes you money
    private void checkStatusType() {
        if (amount == 0) {
            type = PaymentStatusType.EVEN;
        } else if (amount < 0) {
            type = PaymentStatusType.YOU_OWE;
        } else {
            type = PaymentStatusType.SOMEBODY_OWES;
        }
    }

    @Override
    public String toString() {
        if(type == PaymentStatusType.SOMEBODY_OWES){
            return String.format("Owes you %.2f LV", getAmount());
        }
        return String.format("You owe %.2f LV", getAmount());
    }
}
