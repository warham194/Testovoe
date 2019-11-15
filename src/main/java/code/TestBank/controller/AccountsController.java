package code.TestBank.controller;


import code.TestBank.exception.AccountNotFoundException;
import code.TestBank.exception.DublicateIdException;
import code.TestBank.exception.NotEnoughException;
import code.TestBank.exception.TransferException;
import code.TestBank.model.Account;
import code.TestBank.model.Transfer;
import code.TestBank.service.AccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private static final Logger log = LoggerFactory.getLogger(AccountsController.class);

    @Autowired
    private AccountsService accountsService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
        log.info("Creating account {}", account);

        try {
            if (account.getAccountId() <=0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            this.accountsService.createAccount(account);
        } catch (DublicateIdException d) {
            return new ResponseEntity<>(d.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/{accountId}")
    public Account getAccount(@PathVariable int accountId) {
        log.info("Retrieving account for id {}", accountId);
        return this.accountsService.getAccount(accountId);
    }

    @PutMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeTransfer(@RequestBody @Valid Transfer transfer) {
        log.info("Making transfer {}", transfer);

        try {
            this.accountsService.makeTransfer(transfer);
        } catch (AccountNotFoundException a) {
            return new ResponseEntity<>(a.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotEnoughException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TransferException t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/order/history")
    public List<Transfer> getTransfers(){
        return accountsService.listTrasfers();
    }

    @GetMapping(path = "/history/{transferId}")
    public Transfer getTransfer(@PathVariable int transferId) {
        log.info("Retrieving account for id {}", transferId);
        return this.accountsService.getTransfer(transferId);
    }


}
