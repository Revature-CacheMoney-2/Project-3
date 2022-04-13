package com.revature.cachemoney.backend.beans.services;

import java.util.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transaction;
import com.revature.cachemoney.backend.beans.repositories.AccountRepo;
import com.revature.cachemoney.backend.beans.repositories.TransactionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is the service layer for Account requests.
 * 
 * @author Alvin Frierson, Cody Gonsowski, and Jeffrey Lor
 */
@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final String[] accountTypes = { "checking", "savings" };

    @Autowired
    public AccountService(AccountRepo accountRepo, TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    /**
     * Service method to GET *ALL* Accounts.
     * 
     * @return List of Accounts
     */
    public List<Account> getAllAccounts() {
        return accountRepo.findAll();
    }

    /**
     * Service method to GET an Account by Account's ID.
     * 
     * @param accountId of Account to find
     * @param userId    to verify the User owns the Account
     * @return Account associated with the User
     */
    public Optional<Account> getAccountByID(Integer accountId, Integer userId) {
        Optional<Account> returnAccount = accountRepo.findById(accountId);

        if (returnAccount.isPresent()) {
            if (Objects.equals(returnAccount.get().getUser().getUserId(), userId)) {
                return accountRepo.findById(accountId);
            }
        }

        return Optional.empty();
    }

    /**
     * Service method to POST an Account.
     * 
     * @param account of Account to save
     * @param userId  to verify the User owns the Account
     * @return (true | false) if the User owns the Account
     */
    public Boolean postAccount(Account account, Integer userId) {
        if (account.getType().equals(accountTypes[0]) || account.getType().equals(accountTypes[1])) {
            if (account.getUser().getUserId() == userId) {
                accountRepo.save(account);
                return true;
            }
        }
        return false;
    }

    /**
     * Service method to DELETE an Account by Account's ID.
     * 
     * @param accountId of Account to delete
     * @param userId    to verify the User owns the Account
     * @return (true | false) if the User owns the Account
     */
    public Boolean deleteAccountById(Integer accountId, Integer userId) {
        Optional<Account> returnAccount = accountRepo.findById(accountId);

        if (returnAccount.isPresent()) {
            if (Objects.equals(returnAccount.get().getUser().getUserId(), userId)) {
                accountRepo.deleteById(accountId);
                return true;
            }
        }

        return false;
    }

    /**
     * Service method to GET Transactions associated with an Account's ID.
     * 
     * @param accountId of Account to retrieve Transactions from
     * @param userId    to verify the User owns the Account
     * @return List of Transactions associated with the Account
     */
    public List<Transaction> getTransactionsById(Integer accountId, Integer userId) {
        Optional<Account> account = accountRepo.findById(accountId);
        
        if (account.isPresent()) {
            if (Objects.equals(account.get().getUser().getUserId(), userId)) {
                return transactionRepo.findByAccount(accountRepo.getById(accountId));
            }
        }

        return null;
    }

    /******* MONEY HANDLING *******/
    /**
     * Deposit to an amount to an Account provided the Account's ID.
     * Creates a transaction with a custom description associated with the deposit.
     * 
     * @param userId      to verify the User owns the Account
     * @param transaction containing the accountId, the amount, and (optionally) a
     *                    description
     * @return (true | false) based on deposit status
     */
    public Boolean depositToAccount(Integer userId, Transaction transaction) {
        // create a temporary account
        Optional<Account> optAccount = accountRepo.findById(transaction.getAccount().getAccountId());

        if (optAccount.isPresent()) {
            // placeholder account
            Account account = optAccount.get();

            // transaction amount holder
            Double amount = transaction.getTransactionAmount();

            // verify the account belongs to the user
            if (account.getUser().getUserId() == userId) {
                // force decimal to be 2 places
                amount = Math.round(amount * 100) / 100.0;

                // check if the amount is positive
                if (amount >= 0) {
                    // update the balance locally
                    account.setBalance(account.getBalance() + amount);

                    // set a default description if none is provided
                    if (transaction.getDescription() == null || transaction.getDescription().equals("")) {
                        transaction.setDescription("Deposit");
                    }

                    // set the current date
                    transaction.setTransactionDate(new Date());

                    // set new balance & store it
                    transaction.setEndingBalance(account.getBalance());
                    transactionRepo.save(transaction);

                    // store the updated balance
                    accountRepo.save(account);

                    // deposit success
                    return true;
                }
            }
        }

        // deposit fail
        return false;
    }

    /**
     * Withdraw from an amount to an Account provided the Account's ID.
     * Creates a transaction with a custom description associated with the
     * withdrawal.
     * 
     * @param userId      to verify the User owns the Account
     * @param transaction containing the accountId, the amount, and (optionally) a
     *                    description
     * @return (true | false) based on withdrawal status
     */
    public Boolean withdrawFromAccount(Integer userId, Transaction transaction) {
        // create a temporary account
        Optional<Account> optAccount = accountRepo.findById(transaction.getAccount().getAccountId());

        if (optAccount.isPresent()) {
            // placeholder account
            Account account = optAccount.get();
            // transaction amount holder
            Double amount = transaction.getTransactionAmount();

            // verify the account belongs to the user
            if (Objects.equals(account.getUser().getUserId(), userId)) {
                // force decimal to be 2 places
                amount = Math.round(amount * 100) / 100.0;
                // check if the amount is positive
                if ((amount >= 0) && (amount <= account.getBalance())) {
                    // update the balance locally
                    account.setBalance(account.getBalance() - amount);

                    // set a default description if none is provided
                    if (transaction.getDescription() == null || transaction.getDescription().equals("")) {
                        transaction.setDescription("Withdrawal");
                    }

                    // set the current date
                    transaction.setTransactionDate(new Date());

                    // set new balance
                    transaction.setEndingBalance(account.getBalance());

                    // store the amount as a negative
                    transaction.setTransactionAmount(-transaction.getTransactionAmount());

                    // save the transaction
                    transactionRepo.save(transaction);

                    // store the updated balance
                    accountRepo.save(account);

                    // withdraw success
                    return true;
                }
            }
        }

        // withdraw fail
        return false;
    }

    /**
     * Transfer money between two Accounts of the same User.
     * 
     * @param userId          to verify the User owns the Account
     * @param sourceAccountId of Account to transfer from
     * @param destAccountId   of Account to transfer to
     * @param transaction     containing description and amount
     * @return (true | false) based on transfer status
     */
    public Boolean transferBetweenAccountsOfOneUser(Integer userId, Integer sourceAccountId, Integer destAccountId,
            Transaction transaction) {
        // optional accounts
        Optional<Account> source = accountRepo.findById(sourceAccountId);
        Optional<Account> dest = accountRepo.findById(destAccountId);

        // handle if an account exists
        if (source.isPresent() && dest.isPresent()) {
            // copied transaction
            Transaction destTransaction = new Transaction(dest.get(), transaction.getDescription(), null,
                    transaction.getTransactionAmount(), null);

            // original transaction has linked account
            transaction.setAccount(source.get());

            // validates that the source & destination User IDs match
            if (source.get().getUser().getUserId() == dest.get().getUser().getUserId()) {
                // attempt to withdraw & deposit
                if (withdrawFromAccount(userId, transaction) && depositToAccount(userId, destTransaction)) {
                    // transfer success;

                    return true;
                }
            }
        }

        // transfer fail
        return false;
    }

    /**
     *
     * ******************STRICTLY FOR TESTING PURPOSES*********************
     *
     */
    public void deleteAllAccounts() {
        accountRepo.deleteAll();
    }

}
