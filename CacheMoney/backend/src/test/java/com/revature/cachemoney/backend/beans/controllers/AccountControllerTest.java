package com.revature.cachemoney.backend.beans.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transaction;
import com.revature.cachemoney.backend.beans.models.Transfer;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.services.AccountService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit testing of the AccountController class.
 * 
 * @author David Alvarado, Brandon Perrien,
 *         Jeremiah Smith, Alvin Frierson,
 *         Trevor Hughes, Maja Wirkijowska,
 *         Ahmad Rawashdeh, Ibrahima Diallo,
 *         Brian Gardner, Jeffrey Lor,
 *         Mark Young
 */
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @MockBean
    private AccountService accountService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private final AccountController accountController = new AccountController(accountService, mapper);

    private User user;
    private Account account;

    /**
     * Data initialization before each test method.
     */
    @BeforeEach
    void populateData() {
        // set a user for testing
        user = new User("Jon", "Snow", "jonsnow@gmail.com", "theonetrueking", "jonsnowking");
        user.setUserId(1);

        // set an account for testing
        account = new Account("checking", "jonsaccount");
        account.setUser(user);
    }

    /**
     * Data deleted after each method in case changes were made.
     */
    @AfterEach
    void unpopulateData() {
        user = null;
        account = null;
    }

    /**
     * Method test if retrieving an Account by ID is possible
     * when certain criteria is met.
     */
    @Test
    void getAccountByID() throws JsonProcessingException {
        // mocking accountService to return an Optional with our account created here.
        when(accountService.getAccountByID(1, 1)).thenReturn(Optional.of(account));

        // Checking response's http status
        ResponseEntity<String> response = accountController.getAccountByID("test", 1, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // mocking accountService to return an empty optional (failed attempt)
        when(accountService.getAccountByID(1, 1)).thenReturn(Optional.empty());

        // Checking response's http status
        response = accountController.getAccountByID("test", 1, 1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Method test to check if persisting an Account is possible
     * when criteria are met.
     */
    @Test
    void postAccount() throws Exception {
        // mocking accountService to return true.
        when(accountService.postAccount(account, user.getUserId())).thenReturn(true);

        // Checking response's http status
        ResponseEntity<String> response = accountController.postAccount("test", user.getUserId(), account);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // mocking accountService to return false.
        when(accountService.postAccount(account, user.getUserId())).thenReturn(false);

        // Checking response's http status
        response = accountController.postAccount("test", user.getUserId(), account);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Method test to check if deleting an Account is possible
     * when criteria are met.
     */
    @Test
    void deleteAccountById() {
        // mocking accountService to return true.
        when(accountService.deleteAccountById(account.getAccountId(), user.getUserId())).thenReturn(true);

        // Checking response's http status
        ResponseEntity<String> response = accountController.deleteAccountById("test", user.getUserId(),
                account.getAccountId());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // mocking accountService to return false.
        when(accountService.deleteAccountById(account.getAccountId(), user.getUserId())).thenReturn(false);

        // Checking response's http status
        response = accountController.deleteAccountById("test", user.getUserId(), account.getAccountId());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Method test to check if retrieving Transactions is possible
     * when criteria are met.
     */
    @Test
    void getTransactionsById() throws JsonProcessingException {
        // mocking accountService to return an empty list of transactions
        when(accountService.getTransactionsById(account.getAccountId(), user.getUserId()))
                .thenReturn(new LinkedList<Transaction>());

        // Checking response's http status
        ResponseEntity<String> response = accountController.getTransactionsById("test", user.getUserId(),
                account.getAccountId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Method test to check if a deposit is possible
     * when criteria are met.
     */
    @Test
    void deposit() {
        // new transaction creation
        Transaction transaction = new Transaction();

        // mocking accountService to return true
        when(accountService.depositToAccount(user.getUserId(), transaction)).thenReturn(true);

        // Checking response's http status
        ResponseEntity<String> response = accountController.deposit("test", user.getUserId(), transaction);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // mocking accountService to return false
        when(accountService.depositToAccount(user.getUserId(), transaction)).thenReturn(false);

        // Checking response's http status
        response = accountController.deposit("test", user.getUserId(), transaction);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Method test to check if a withdrawal is possible
     * when criteria are met.
     */
    @Test
    void withdraw() {
        // new transaction creation
        Transaction transaction = new Transaction();

        // mocking accountService to return true
        when(accountService.withdrawFromAccount(user.getUserId(), transaction)).thenReturn(true);

        // Checking response's http status
        ResponseEntity<String> response = accountController.withdraw("test", user.getUserId(), transaction);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // mocking accountService to return false
        when(accountService.withdrawFromAccount(user.getUserId(), transaction)).thenReturn(false);

        // Checking response's http status
        response = accountController.withdraw("test", user.getUserId(), transaction);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
