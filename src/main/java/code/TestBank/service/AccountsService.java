package code.TestBank.service;

import code.TestBank.model.Account;
import code.TestBank.model.Transfer;

import java.util.List;

public interface AccountsService {
    void createAccount(Account account);
    Account getAccount(int accountId) ;
    void makeTransfer(Transfer transfer);
    List<Transfer> listTrasfers();
    Transfer getTransfer(int id);
}
