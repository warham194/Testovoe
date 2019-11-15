package code.TestBank.service;


import code.TestBank.exception.AccountNotFoundException;
import code.TestBank.exception.NotEnoughException;
import code.TestBank.model.Account;
import code.TestBank.model.Transfer;

interface TransferValidator {

    void validate(final Account accountFrom, final Account accountTo, final Transfer transfer) throws AccountNotFoundException, NotEnoughException;

}
