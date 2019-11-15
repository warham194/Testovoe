package code.TestBank.model;





public class AccountUpdate {

    private final int accountId;
    private final double amount;

    public AccountUpdate(int accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "AccountUpdate{" +
                "accountId='" + accountId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
