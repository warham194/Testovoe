package code.TestBank.service;

import code.TestBank.exception.AccountNotFoundException;
import code.TestBank.exception.DublicateIdException;
import code.TestBank.exception.NotEnoughException;
import code.TestBank.exception.TransferException;
import code.TestBank.model.Account;
import code.TestBank.model.AccountUpdate;
import code.TestBank.model.Transfer;
import code.TestBank.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransferValidator transferValidator;




    public void createAccount(Account account) throws DublicateIdException {
        this.accountsRepository.createAccount(account);
    }

    public Account getAccount(int accountId) {
        return this.accountsRepository.getAccount(accountId);
    }


    public void makeTransfer(Transfer transfer) throws AccountNotFoundException, NotEnoughException, TransferException {

        final Account accountFrom = accountsRepository.getAccount(transfer.getAccountFromId());
        final Account accountTo = accountsRepository.getAccount(transfer.getAccountToId());
        final double amount = transfer.getAmount();


        transferValidator.validate(accountFrom, accountTo, transfer);

        //ideally atomic operation in production
        boolean successful = accountsRepository.updateAccounts(Arrays.asList(
                new AccountUpdate(accountFrom.getAccountId(), -amount),
                new AccountUpdate(accountTo.getAccountId(), amount)
        ));
        if (successful){
            log.info(accountFrom + " The transfer to the account with ID " + accountTo.getAccountId() + " is now complete for the amount of " + transfer.getAmount() + ".");
            log.info(accountTo + " The account with ID + " + accountFrom.getAccountId() + " has transferred " + transfer.getAmount() + " into your account.");
            accountsRepository.creatTransfer(transfer);
        }
    }

    @Override
    public List<Transfer> listTrasfers() {
        return new ArrayList<>(accountsRepository.transferList());
    }

    @Override
    public Transfer getTransfer(int id) {
        return accountsRepository.getTransfer(id);
    }
}