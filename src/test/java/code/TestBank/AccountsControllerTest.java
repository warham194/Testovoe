package code.TestBank;



import code.TestBank.model.Account;
import code.TestBank.model.Transfer;
import code.TestBank.repository.AccountsRepository;
import code.TestBank.service.AccountsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccountsControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private AccountsService accountsService;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        // Reset the existing accounts before each test.
        accountsRepository.clearAccounts();
    }

    @Test
    public void createAccount() throws Exception {
        createAccountWithContent("{\"accountId\":\"23\",\"balance\":1000}").andExpect(status().isCreated());
        Account account = accountsService.getAccount(23);
        assertThat(account.getBalance() == 1000);
        assertThat(account.getAccountId() ==23);
    }

    @Test
    public void createDuplicateAccount() throws Exception {
        createAccountWithContent("{\"accountId\":\"23\",\"balance\":1000}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"23\",\"balance\":1000}").andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNoAccountId() throws Exception {
        createAccountWithContent("{\"balance\":1000}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNoBody() throws Exception {
        this.mockMvc.perform(post("accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createAccountNegativeBalance() throws Exception {
        createAccountWithContent("{\"accountId\":\"23\",\"balance\":-1000}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountEmptyAccountId() throws Exception {
        createAccountWithContent("{\"accountId\":\"\",\"balance\":1000}").andExpect(status().isBadRequest());
    }

    private ResultActions createAccountWithContent(final String content) throws Exception {
        return this.mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    @Test
    public void getAccount() throws Exception {
        Account account = new Account(100500, 800.0);
        accountsService.createAccount(account);
        verifyAccountBalance(100500,800.0);
    }

    @Test
    public void makeTransferAccountFromNotFound() throws Exception {
        createAccountWithContent("{\"accountId\":\"23\",\"balance\":1000}").andExpect(status().isCreated());
        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"23\",\"amount\":1000}").andExpect(status().isNotFound());
    }

    @Test
    public void makeTransferAccountToNotFound() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":75}").andExpect(status().isCreated());
        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"23\",\"amount\":20}")
                .andExpect(status().isNotFound());
    }

    @Test
    public void makeTransferSameAccount() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":1000}").andExpect(status().isCreated());
        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"1\",\"amount\":1000}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void makeTransferOverdraft() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":20.50}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"2\",\"balance\":1000}").andExpect(status().isCreated());

        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":21}")
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void makeTransferNegativeAmount() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":100.50}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"2\",\"balance\":1000.50}").andExpect(status().isCreated());

        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":-50}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void makeTransferEmptyBody() throws Exception {
        makeTransferWithContent("{}")
                .andExpect(status().isBadRequest());
    }

    @Test
    public void makeTransferNoBody() throws Exception {
        this.mockMvc.perform(put("/accounts/transfer").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void makeTransferZeroBalanceAfterTransfer() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":2000}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"2\",\"balance\":100}").andExpect(status().isCreated());

        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":2000}")
                .andExpect(status().isOk());

        verifyAccountBalance(1, 0.0);
        verifyAccountBalance(2, 2100.0);
    }

    @Test
    public void makeTransferBetweenAccountsPositiveBalanceAfterTransfer() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":700}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"2\",\"balance\":450}").andExpect(status().isCreated());

        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":30}")
                .andExpect(status().isOk());

        verifyAccountBalance(1, 670);
        verifyAccountBalance(2, 480);
    }

    @Test
    public void getTransfer() throws Exception {
        createAccountWithContent("{\"accountId\":\"1\",\"balance\":700}").andExpect(status().isCreated());
        createAccountWithContent("{\"accountId\":\"2\",\"balance\":450}").andExpect(status().isCreated());
        makeTransferWithContent("{\"accountFromId\":\"1\",\"accountToId\":\"2\",\"amount\":30}").andExpect(status().isOk());
        accountsService.getTransfer(1);
        Transfer transfer = new Transfer(1,2,30);
        assertThat(accountsService.getTransfer(1).equals(transfer));

    }

    private ResultActions makeTransferWithContent(String content) throws Exception {
        return this.mockMvc.perform(
                put("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
    }

    private void verifyAccountBalance(int accountId, double balance) throws Exception {
        this.mockMvc.perform(get("/accounts/" + accountId))
                .andExpect(status().isOk())
                .andExpect(
                        content().string("{\"accountId\":" + accountId + ",\"balance\":"+balance+"}"));
    }

}

