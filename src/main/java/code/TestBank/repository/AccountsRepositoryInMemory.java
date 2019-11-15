package code.TestBank.repository;

import code.TestBank.exception.DublicateIdException;
import code.TestBank.model.Account;
import code.TestBank.model.AccountUpdate;
import code.TestBank.model.Transfer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Transactional
public class AccountsRepositoryInMemory implements AccountsRepository {

    private static AtomicInteger counterTransaction = new AtomicInteger(0);
    private final Map<Integer, Account> accounts = new ConcurrentHashMap<>();
    private Map<Integer, Transfer> transferMap = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null ) {
            throw new DublicateIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(int accountId) {
        return accounts.get(accountId);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }


    @Override
    public boolean updateAccounts(List<AccountUpdate> accountUpdates) {
        accountUpdates.forEach(this::updateAccount);
        return true;
    }

    @Override
    public void creatTransfer(Transfer transfer) {
        transferMap.put(counterTransaction.incrementAndGet(), transfer);
    }

    @Override
    public List<Transfer> transferList() {
        return new ArrayList<>(transferMap.values());
    }

    @Override
    public Transfer getTransfer(int id) {
        return transferMap.get(id);
    }

    private void updateAccount(final AccountUpdate accountUpdate) {
        final int accountId = accountUpdate.getAccountId();
        accounts.computeIfPresent(accountId, (key, account) -> {
            account.setBalance(account.getBalance() + accountUpdate.getAmount());
            return account;
        });
    }

}
