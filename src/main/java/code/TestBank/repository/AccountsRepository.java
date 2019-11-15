package code.TestBank.repository;



import code.TestBank.model.Account;
import code.TestBank.model.AccountUpdate;
import code.TestBank.model.Transfer;

import java.util.List;

public interface AccountsRepository {

  void createAccount(Account account);
  Account getAccount(int accountId);
  void clearAccounts();
  boolean updateAccounts(List<AccountUpdate> accountUpdates);
  void creatTransfer(Transfer transfer);
  List<Transfer> transferList();
  Transfer getTransfer(int id);

}
