package com.revature.cachemoney.backend.beans.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transaction;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.repositories.AccountRepo;
import com.revature.cachemoney.backend.beans.repositories.TransactionRepo;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit testing of the AccountService class.
 * 
 * @author David Alvarado, Brandon Perrien,
 *         Jeremiah Smith, Alvin Frierson,
 *         Trevor Hughes, Maja Wirkijowska,
 *         Ahmad Rawashdeh, Ibrahima Diallo,
 *         Brian Gardner, Jeffrey Lor,
 *         Mark Young
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @MockBean
    private AccountRepo accountRepo;

    @MockBean
    private TransactionRepo transactionRepo;

    private User tempUser;

    private List<Account> validAccounts;

    /**
     * Data initialization for local variables.
     * With these tests, no data is persisted to database.
     * We MOCKED the return values of the repo functions
     * which are the functions in charge of data persistence.
     */
    @BeforeEach
    void populateDBWithUserandAccounts() {
        if (userService.getAllUsers().size() != 0) {
            if (accountService.getAllAccounts().size() != 0) {
                accountService.deleteAllAccounts();
            }
            userService.deleteAllUsers();
        }
        tempUser = new User("Hank", "Hill", "hankaccounthill@gmail.com", "abcd1234", "accounttest");
        tempUser.setUserId(1);

        // create accounts (no valid user model under attribute "user")
        Account checkingAcc = new Account("checking");
        Account savingsAcc = new Account("savings");
        Account checkingAccWithNickname = new Account("checking", "secret account");

        // populate user field with tempUser
        checkingAcc.setUser(tempUser);
        savingsAcc.setUser(tempUser);
        checkingAccWithNickname.setUser(tempUser);

        // setting account ids
        checkingAcc.setAccountId(1);
        savingsAcc.setAccountId(2);
        checkingAccWithNickname.setAccountId(3);

        // add accounts to list.
        validAccounts = new LinkedList<>();
        validAccounts.add(checkingAcc);
        validAccounts.add(savingsAcc);
        validAccounts.add(checkingAccWithNickname);
    }

    /**
     * Test to see if we can retrieve all Accounts.
     */
    @Test
    void getAllAccounts() {
        // mocking repo so when findAll() methos is call, we return validAccounts
        when(accountRepo.findAll()).thenReturn(validAccounts);

        // check size of the list returned with what we expect (our validAccount list)
        assertEquals(validAccounts.size(), accountService.getAllAccounts().size());
    }

    /**
     * Checking that we can retrieve an Account using the accountId.
     */
    @Test
    void getAccountByID() {
        // mocking account repo to return the first account in our validAccount list
        // when passed in the accountId that belongs to the first account in the
        // validAccount list.
        when(accountRepo.findById(validAccounts.get(0).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(0)));

        // checking that the account is returned by the method
        assertTrue(accountService.getAccountByID(validAccounts.get(0).getAccountId(), tempUser.getUserId())
                .isPresent());

        // checking that no account is returned by method
        assertFalse(accountService.getAccountByID(0, 1).isPresent());
    }

    /**
     * Checking that we can delete an Account with a valid accountId.
     */
    @Test
    void deleteAccountById() {
        // mocking account repo to return an empty Optional list
        when(accountRepo.findById(validAccounts.get(0).getAccountId())).thenReturn(Optional.empty());

        // checking that its not present when method is invoked
        assertFalse(accountService.deleteAccountById(
                validAccounts.get(0).getAccountId(),
                tempUser.getUserId()));

        // mocking account repo to return first account in our validAccount list
        when(accountRepo.findById(validAccounts.get(0).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(0)));

        // checking that deleting an account fails with an incorrect userID
        assertFalse(accountService.deleteAccountById(
                validAccounts.get(0).getAccountId(),
                -1));

        // checking ability to delete the account with valid accountId and userId
        assertTrue(accountService.deleteAccountById(
                validAccounts.get(0).getAccountId(),
                tempUser.getUserId()));
    }

    /**
     * Checking that we are able to get Transactions with transactionId.
     */
    @Test
    void getTransactionsById() {
        // NOTE: THE DATE VALUE BEFORE THE TRANSACTION IS PERSISTED IS DIFFERENT
        // FROM WHAT YOU GET FROM THE DATABASE. THIS IS WHY "toStringWithoutDate"
        // WAS CREATED. AS OF 3/13/2022, THE DATES MATCH BUT ARE FORMATTED DIFFERENTLY.

        // mocking repo to return empty optional
        when(accountRepo.findById(validAccounts.get(0).getAccountId())).thenReturn(Optional.empty());

        // asserting method returns empty optional
        assertNull(accountService.getTransactionsById(
                validAccounts.get(0).getAccountId(),
                tempUser.getUserId()));

        // mocking repo to return a vlid account and an empty list of transactions
        when(accountRepo.findById(validAccounts.get(0).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(0)));
        when(transactionRepo.findByAccount(validAccounts.get(0))).thenReturn(new LinkedList<>());

        // asserting that the output equals an empty transaction list.
        assertEquals(new LinkedList<Transaction>(), accountService.getTransactionsById(
                validAccounts.get(0).getAccountId(),
                tempUser.getUserId()));

        // asserting an empty optional is returned since an invalid username was
        // inserted.
        assertNull(accountService.getTransactionsById(
                validAccounts.get(0).getAccountId(),
                -1));
    }

    /**
     * Checking that we can withdraw from an Account
     * when certain criteria is met
     * (i.e withdrawing from an account with sufficient funds, etc...)
     */
    @Test
    void withdrawFromAccount() {
        // Withdrawal transaction created
        Transaction withdrawal = new Transaction(validAccounts.get(0),
                "withdrawal after my initial deposit", new Date(), 10.50, 2.50);

        // Mocking repo to return an empty optional when accountId= 1
        when(accountRepo.findById(1)).thenReturn(Optional.empty());

        // Check that we get a false value when withdrawing from account
        assertFalse(accountService.withdrawFromAccount(tempUser.getUserId(), withdrawal));

        // mocking a valid account
        when(accountRepo.findById(1)).thenReturn(Optional.of(validAccounts.get(0)));

        // check that we get a false value for using an incorrect userId value
        assertFalse(accountService.withdrawFromAccount(-1, withdrawal));

        // setting withdrawal amount to a negative value which is not allows
        withdrawal.setTransactionAmount(-1.00);
        assertFalse(accountService.withdrawFromAccount(
                validAccounts.get(0).getUser().getUserId(),
                withdrawal));

        // checking that a valid withdrawal fails due to insufficient funds.
        withdrawal.setTransactionAmount(1.00);
        assertFalse(accountService.withdrawFromAccount(
                validAccounts.get(0).getUser().getUserId(),
                withdrawal));

        // adding sufficient funds
        // mocking transactionRepo and accountRepo valid
        // return values to allow withdrawal.
        validAccounts.get(0).setBalance(1000.00);
        withdrawal.setTransactionAmount(1.00);
        withdrawal.setDescription(null);
        when(transactionRepo.save(withdrawal)).thenReturn(withdrawal);
        when(accountRepo.save(validAccounts.get(0))).thenReturn(validAccounts.get(0));

        assertTrue(accountService.withdrawFromAccount(
                validAccounts.get(0).getUser().getUserId(),
                withdrawal));

        // the following 2 test just test for null/empty
        // string value for description is the
        // withdrawal transaction.
        withdrawal.setDescription("");
        assertTrue(accountService.withdrawFromAccount(
                validAccounts.get(0).getUser().getUserId(),
                withdrawal));

        withdrawal.setDescription("test");
        assertTrue(accountService.withdrawFromAccount(
                validAccounts.get(0).getUser().getUserId(),
                withdrawal));
    }

    /**
     * Checking that we can deposit to an Account.
     */
    @Test
    void depositToAccount() {
        // Transaction creation
        Transaction deposit = new Transaction(validAccounts.get(0),
                "deposit", new Date(), 10.50, 2.50);

        // mocking that an account does not exist (returning empty optional)
        // asserting method should return false.
        when(accountRepo.findById(1)).thenReturn(Optional.empty());
        assertFalse(accountService.depositToAccount(tempUser.getUserId(), deposit));

        // mocking a valid account but using an incorrect userId value.
        // asserting method should return false
        when(accountRepo.findById(1)).thenReturn(Optional.of(validAccounts.get(0)));
        assertFalse(accountService.depositToAccount(-1, deposit));

        // checking that depositing a negative number is not possible
        // method should return false.
        deposit.setTransactionAmount(-1.00);
        assertFalse(accountService.depositToAccount(
                validAccounts.get(0).getUser().getUserId(),
                deposit));

        // checking that you can deposit to an account with existing funds.
        validAccounts.get(0).setBalance(1000.00);
        deposit.setTransactionAmount(1.00);
        deposit.setDescription(null);
        when(transactionRepo.save(deposit)).thenReturn(deposit);
        when(accountRepo.save(validAccounts.get(0))).thenReturn(validAccounts.get(0));
        assertTrue(accountService.depositToAccount(
                validAccounts.get(0).getUser().getUserId(),
                deposit));

        // following 2 test check that a deposit is successful
        // regardless if description field is null/empty
        deposit.setDescription("");
        assertTrue(accountService.depositToAccount(
                validAccounts.get(0).getUser().getUserId(),
                deposit));

        deposit.setDescription("deposit test");
        assertTrue(accountService.depositToAccount(
                validAccounts.get(0).getUser().getUserId(),
                deposit));
    }

    /**
     * ! unfinished
     * Due to time constraints, this test
     * only checks the cases that the method
     * AccountService.transferBetweenAccountsOfOneUser
     * should not allow a transfer to take place.
     */
    @Test
    void transferBetweenAccountsOfOneUser() {
        // Transaction creation
        Transaction transaction = new Transaction(validAccounts.get(0),
                "transfer", new Date(), 10.50, 2.50);

        // Mocking empty source and destination accounts.
        // Method should return false
        when(accountRepo.findById(validAccounts.get(0).getAccountId())).thenReturn(Optional.empty());
        when(accountRepo.findById(validAccounts.get(1).getAccountId())).thenReturn(Optional.empty());
        assertFalse(accountService.transferBetweenAccountsOfOneUser(
                tempUser.getUserId(),
                validAccounts.get(0).getAccountId(),
                validAccounts.get(1).getAccountId(),
                transaction));

        // Mocking that source account is present, but destination account is not.
        // Method should return false
        when(accountRepo.findById(validAccounts.get(0).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(0)));
        assertFalse(accountService.transferBetweenAccountsOfOneUser(
                tempUser.getUserId(),
                validAccounts.get(0).getAccountId(),
                validAccounts.get(1).getAccountId(),
                transaction));

        // Mocking that the dest account is present, but the source account is not.
        // Method should return false
        when(accountRepo.findById(validAccounts.get(0).getAccountId())).thenReturn(Optional.empty());
        when(accountRepo.findById(validAccounts.get(1).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(1)));
        assertFalse(accountService.transferBetweenAccountsOfOneUser(
                tempUser.getUserId(),
                validAccounts.get(0).getAccountId(),
                validAccounts.get(1).getAccountId(),
                transaction));

        // Checking that an incorrect userId prevents a transaction
        // method should return false
        validAccounts.get(0).getUser().setUserId(-1);
        when(accountRepo.findById(validAccounts.get(0).getAccountId()))
                .thenReturn(Optional.of(validAccounts.get(0)));
        assertFalse(accountService.transferBetweenAccountsOfOneUser(
                tempUser.getUserId(),
                validAccounts.get(0).getAccountId(),
                validAccounts.get(1).getAccountId(),
                transaction));
    }

    /**
     * Nested class checks if Accounts can be posted to database.
     */
    @Nested
    class TestPostAccount {
        /**
         * Data initialization:
         * a valid user must be in database, so it's persisted here.
         */
        @BeforeEach
        void populateDB() throws QrGenerationException {
            if (userService.getAllUsers().size() != 0) {
                if (accountService.getAllAccounts().size() != 0) {
                    accountService.deleteAllAccounts();
                }
                userService.deleteAllUsers();

                tempUser = new User("Hank", "Hill", "hankaccounthill@gmail.com", "abcd1234",
                        "accounttest");
                userService.postUser(tempUser);
            } else {
                tempUser = new User("Hank", "Hill", "hankaccounthill@gmail.com", "abcd1234",
                        "accounttest");
                userService.postUser(tempUser);
            }
        }

        /**
         * Any changes made to tempUser and in the database are deleted after test.
         */
        @AfterEach
        void deleteDBData() {
            accountService.deleteAllAccounts();
            userService.deleteAllUsers();
            tempUser = null;
        }

        /**
         * Method test to check if an account is persisted to database.
         */
        @Test
        void postAccount() {
            // valid account type
            Account testChecking = new Account("checking");

            // invalid account type
            Account testIncorrectType = new Account("blahblah");

            // both accounts are set to belong to tempUser
            testChecking.setUser(tempUser);
            testIncorrectType.setUser(tempUser);

            // invalid account fail check
            assertFalse(accountService.postAccount(testIncorrectType, tempUser.getUserId()));

            // valid account pass check
            assertTrue(accountService.postAccount(testChecking, tempUser.getUserId()));

            // invalid userId check
            assertFalse(accountService.postAccount(testChecking, -1));
        }
    }
}
