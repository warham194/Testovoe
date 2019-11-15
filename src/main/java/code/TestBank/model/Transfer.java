package code.TestBank.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


public class Transfer {

    @NotNull
    private int accountFromId;

    @NotNull
    private int accountToId;

    @NotNull
    @Min(value = 1)
    private double amount;

    private LocalDateTime dateTime;

    @JsonCreator
    public Transfer(@JsonProperty("accountFromId") int accountFromId,
                    @JsonProperty("accountToId") int accountToId,
                    @JsonProperty("amount") double amount){
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "accountFromId='" + accountFromId + '\'' +
                ", accountToId='" + accountToId + '\'' +
                ", amount=" + amount +
                ", dateTime=" + dateTime +
                '}';
    }
}
