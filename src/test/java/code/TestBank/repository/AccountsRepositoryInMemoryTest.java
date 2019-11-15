package code.TestBank.repository;

import code.TestBank.model.Account;
import code.TestBank.model.AccountUpdate;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class AccountsRepositoryInMemoryTest {

    @Autowired
    private AccountsRepository accountsRepository;


    @Test
    public void updateAccountsBatch_should_updateAllAccounts() throws Exception {

        accountsRepository.createAccount(new Account(1, 0));
        accountsRepository.createAccount(new Account(2, 70.0));

        List<AccountUpdate> accountUpdates = Arrays.asList(
                new AccountUpdate(1, 0),
                new AccountUpdate(2, -60.0)
        );

        accountsRepository.updateAccounts(accountUpdates);
        assertThat(accountsRepository.getAccount(1).getBalance()).isEqualTo(0);
        assertThat(accountsRepository.getAccount(2).getBalance()).isEqualTo(10.0);
    }
}
