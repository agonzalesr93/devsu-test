package com.agonzales.account.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agonzales.account.dto.controller.transaction.AccountCreateRequest;
import com.agonzales.account.dto.controller.transaction.TransactionCreateRequest;
import com.agonzales.account.dto.domain.Account;
import com.agonzales.account.dto.domain.AccountStatus;
import com.agonzales.account.dto.domain.AccountType;
import com.agonzales.account.dto.domain.Client;
import com.agonzales.account.service.AccountService;
import com.agonzales.account.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void Given_NonExistentAccountNumber_When_TransactionIsCreated_Then_Return404() throws Exception {
        //Given
        String nonExistentAccountNumber = "9999999";

        //When
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setAmount(BigDecimal.valueOf(100));

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setAccountNumber(nonExistentAccountNumber);

        createRequest.setAccount(accountCreateRequest);

        ResultActions perform = mockMvc.perform(post("/movimientos")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(createRequest)));

        //Then
        perform.andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Account not found"));
    }

    @Test
    void Given_AccountIsInactive_When_TransactionIsCreated_Then_Return400() throws Exception {
        //Given
        String inactiveAccountNumber = "9999999";

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber(inactiveAccountNumber);
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.INACTIVO);

        Client client = new Client();
        client.setId(99556);

        account.setClient(client);

        accountService.create(account);

        //When
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setAmount(BigDecimal.valueOf(100));

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setAccountNumber(inactiveAccountNumber);

        createRequest.setAccount(accountCreateRequest);

        ResultActions perform = mockMvc.perform(post("/movimientos")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(createRequest)));

        //Then
        perform.andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Account must be active"));

    }

    @Test
    void Given_AccountHasInsufficientFunds_When_TransactionIsCreated_Then_Return400() throws Exception {
        //Given
        String accountNumber = "9999999";

        Account account = new Account();
        account.setId(1);
        account.setAccountNumber(accountNumber);
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(100));
        account.setStatus(AccountStatus.ACTIVO);

        Client client = new Client();
        client.setId(99556);

        account.setClient(client);

        accountService.create(account);

        //When
        TransactionCreateRequest createRequest = new TransactionCreateRequest();
        createRequest.setAmount(BigDecimal.valueOf(-150));

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setAccountNumber(accountNumber);

        createRequest.setAccount(accountCreateRequest);

        ResultActions perform = mockMvc.perform(post("/movimientos")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(createRequest)));

        //Then
        perform.andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Saldo no disponible"));

    }


    @Test
    void When_TransactionIsCreated_Then_ReturnOk() throws Exception {
        Account account = new Account();
        account.setId(1);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.AHORROS);
        account.setInitialBalance(BigDecimal.valueOf(500));
        account.setStatus(AccountStatus.ACTIVO);

        Client client = new Client();
        client.setId(99556);

        account.setClient(client);

        accountService.create(account);

        TransactionCreateRequest transaction = new TransactionCreateRequest();
        transaction.setAmount(BigDecimal.valueOf(100));

        AccountCreateRequest accountCreateRequest = new AccountCreateRequest();
        accountCreateRequest.setAccountNumber("123456789");

        transaction.setAccount(accountCreateRequest);

        mockMvc.perform(post("/movimientos")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(objectMapper.writeValueAsString(transaction)))
          .andExpect(status().isCreated());
    }
}
