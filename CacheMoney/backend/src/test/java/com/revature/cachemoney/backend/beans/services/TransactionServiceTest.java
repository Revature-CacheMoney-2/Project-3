package com.revature.cachemoney.backend.beans.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transaction;
import com.revature.cachemoney.backend.beans.models.User;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit testing of the TransactionService class.
 * 
 * @author David Alvarado, Brandon Perrien,
 *         Jeremiah Smith, Alvin Frierson,
 *         Trevor Hughes, Maja Wirkijowska,
 *         Ahmad Rawashdeh, Ibrahima Diallo,
 *         Brian Gardner
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransactionServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    private Account account;
    private List<Transaction> transactionList;
    private User user;

    /**
     * Method initializes local variables and deletes any data in database
     * before each method test.
     */
    @BeforeEach
    void populateLocalandDBData() throws QrGenerationException {
        if (transactionService.getAllTransactions().size() != 0) {
            transactionService.deleteAllTransactions();
        }

        if (accountService.getAllAccounts().size() != 0) {
            accountService.deleteAllAccounts();
        }

        if (userService.getAllUsers().size() != 0) {
            userService.deleteAllUsers();
        }

        // initial conditions created.
        transactionList = new LinkedList<>();
        user = new User("Hank", "Hill", "hank.hill@gmail.com", "abcd1234", "propanemoney");
        userService.postUser(user);

        account = new Account("checking", "secret account");
        account.setUser(user);
        accountService.postAccount(account, user.getUserId());

        // First deposit made
        Transaction depositOne = new Transaction(account,
                "my first deposit", null, 1.00, null);
        accountService.depositToAccount(user.getUserId(), depositOne);
        transactionList.add(depositOne);

        // updating the status of account so the next transaction has up-to-date data.
        if (accountService.getAccountByID(account.getAccountId(), user.getUserId()).isPresent()) {
            account = accountService.getAccountByID(account.getAccountId(), user.getUserId()).get();
        }

        // Second deposit made
        Transaction depositTwo = new Transaction(account,
                "my second deposit", null, 20.00, null);
        accountService.depositToAccount(user.getUserId(), depositTwo);
        transactionList.add(depositTwo);

        // updating the status of account so the next transaction has up-to-date data.
        if (accountService.getAccountByID(account.getAccountId(), user.getUserId()).isPresent()) {
            account = accountService.getAccountByID(account.getAccountId(), user.getUserId()).get();
        }

        // withdrawal conducted
        Transaction withdrawal = new Transaction(account,
                "my first withdrawal", null, 1.00, null);
        accountService.withdrawFromAccount(user.getUserId(), withdrawal);
        transactionList.add(withdrawal);
    }

    /**
     * Method deletes all data in database and from local variables.
     */
    @AfterEach
    void depopulateLocalAndDBData() {
        if (transactionService.getAllTransactions().size() != 0) {
            transactionService.deleteAllTransactions();
        }

        if (accountService.getAllAccounts().size() != 0) {
            accountService.deleteAllAccounts();
        }

        if (userService.getAllUsers().size() != 0) {
            userService.deleteAllUsers();
        }

        user = null;
        account = null;
        transactionList = null;
    }

    /**
     * Test method checks if we are able to succesfully
     * retrieve the three transactions persisted to database
     * using the transactionList.
     */
    @Test
    void getAllTransactions() {
        assertEquals(transactionList.size(), transactionService.getAllTransactions().size());
    }

    /**
     * Check that we are able to retrieve a Transaction
     * by a transactionId and userId.
     */
    @Test
    void getTransactionById() {
        // we check that the ending balances are the same
        for (Transaction transaction : transactionList) {
            assertEquals(transaction.getEndingBalance(),
                    transactionService.getTransactionById(
                            transaction.getTransactionId(),
                            user.getUserId()).get().getEndingBalance());
        }

        assertFalse(transactionService.getTransactionById(-1, user.getUserId()).isPresent());
        assertFalse(transactionService.getTransactionById(transactionList.get(0).getTransactionId(), -1).isPresent());
        assertFalse(transactionService.getTransactionById(-1, -1).isPresent());
    }

    /**
     * Check to see if we can succesfully delete a Transaction
     * with a valid transactionID and userID.
     */
    @Test
    void deleteTransactionById() {
        assertFalse(transactionService.deleteTransactionById(-1, -1));
        assertFalse(transactionService.deleteTransactionById(-1, user.getUserId()));
        assertFalse(transactionService.deleteTransactionById(transactionList.get(0).getTransactionId(), -1));
        assertTrue(
                transactionService.deleteTransactionById(transactionList.get(0).getTransactionId(), user.getUserId()));
    }
}
