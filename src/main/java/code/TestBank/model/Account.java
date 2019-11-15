package code.TestBank.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class Account {

    @NotNull
    private final int accountId;

    @NotNull
    @Min(value = 0)
    private double balance;

    public Account(int accountId) {
        this.accountId = accountId;
        this.balance = 0.0;
    }

    @JsonCreator
    public Account(@JsonProperty("accountId") int accountId,
                   @JsonProperty("balance") double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", balance=" + balance +
                '}';
    }
}
