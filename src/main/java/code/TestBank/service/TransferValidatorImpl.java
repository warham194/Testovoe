package code.TestBank.service;


import code.TestBank.exception.AccountNotFoundException;
import code.TestBank.exception.NotEnoughException;
import code.TestBank.exception.TransferException;
import code.TestBank.model.Account;
import code.TestBank.model.Transfer;
import org.springframework.stereotype.Component;


@Component
public class TransferValidatorImpl implements TransferValidator {

    public void validate(final Account currAccountFrom, final Account currAccountTo, final Transfer transfer)
            throws AccountNotFoundException, NotEnoughException, TransferException {

        if (currAccountFrom == null){
            throw new AccountNotFoundException("Account " + transfer.getAccountFromId() + " not found.");
        }

        if (currAccountTo == null) {
            throw new AccountNotFoundException("Account " + transfer.getAccountToId() + " not found.");
        }

        if (sameAccount(transfer)){
            throw new TransferException("Transfer to self not permitted.");
        }

        if (!enoughFunds(currAccountFrom, transfer.getAmount())){
            throw new NotEnoughException("Not enough funds on account " + currAccountFrom.getAccountId() + " balance="+currAccountFrom.getBalance());
        }
    }

    private boolean sameAccount(final Transfer transfer) {
        return transfer.getAccountFromId() == transfer.getAccountToId();
    }


    private boolean enoughFunds(final Account account, final double amount) {
        return (account.getBalance() - amount) >= 0.0;
    }

}
