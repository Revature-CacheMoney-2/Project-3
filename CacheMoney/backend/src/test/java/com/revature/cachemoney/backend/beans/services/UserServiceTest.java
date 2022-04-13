package com.revature.cachemoney.backend.beans.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.User;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit testing of the UserService class.
 * 
 * @author David Alvarado, Brandon Perrien,
 *         Jeremiah Smith, Alvin Frierson,
 *         Trevor Hughes, Maja Wirkijowska,
 *         Ahmad Rawashdeh, Ibrahima Diallo,
 *         Brian Gardner
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    private static List<User> validUserList;
    private static List<User> nullValueUserList;
    private static List<User> emptyStringUserList;

    /**
     * Set up data for all tests.
     * nullValueUserList initialized and populated with data.
     * emptyStringUserList initialized and populated with data.
     */
    @BeforeAll
    public static void dataInit() {
        nullValueUserList = new LinkedList<>();
        emptyStringUserList = new LinkedList<>();

        User nullFirstName = new User(null, "Simpson", "homer.simpson@gmail.com", "abcd1234", "hmmdonut");
        User nullLastName = new User("Lisa", null, "lisa.simpson@gmail.com", "abcd1234", "lisa_blues");
        User nullEmail = new User("Bart", "Simpson", null, "abcd1234", "eatmyshorts");
        User nullPassword = new User("Marge", "Simpson", "marge.simpson@gmail.com", null, "MmMmMmMm");
        User nullUsername = new User("Maggie", "Simpson", "maggie.simpson@gmail.com", "abcd1234", null);
        nullValueUserList.add(nullFirstName);
        nullValueUserList.add(nullLastName);
        nullValueUserList.add(nullEmail);
        nullValueUserList.add(nullPassword);
        nullValueUserList.add(nullUsername);

        User emptyFirstName = new User("", "Griffin", "peter.griffin@gmail.com", "abcd1234", "birdistheword");
        User emptyLastName = new User("Lois", "", "lois.griffin@gmail.com", "abcd1234", "therealest");
        User emptyEmail = new User("Stewie", "Griffin", "", "abcd1234", "worlddomination");
        User emptyPassword = new User("Chris", "Griffin", "chris.griffin@gmail.com", "", "crayon123");
        User emptyUsername = new User("Brian", "Griffin", "brian.griffin@gmail.com", "abcd1234", "");
        emptyStringUserList.add(emptyFirstName);
        emptyStringUserList.add(emptyLastName);
        emptyStringUserList.add(emptyEmail);
        emptyStringUserList.add(emptyPassword);
        emptyStringUserList.add(emptyUsername);
    }

    /**
     * Setup initial data before each test conducted.
     * Valid Users are placed in a list and
     * persisted to database only if the database is empty.
     */
    @BeforeEach
    void setupDB() throws QrGenerationException {
        validUserList = new LinkedList<>();
        User user1 = new User("Hank", "Hill", "hank.hill@gmail.com", "abcd1234", "propanemoney");
        User user2 = new User("Peggy", "Hill", "peggy.hill@gmail.com", "abcd1234", "best_sub_teacher");
        User user3 = new User("Bobby", "Hill", "bobby.hill@gmail.com", "abcd1234", "bobbytrill");
        User user4 = new User("Kahn", "Souphanousinphone", "kahn@gmail.com", "abcd1234", "iamnumberone");
        User user5 = new User("Dale", "Gribble", "rusty.shackleford@gmail.com", "abcd1234", "rusty123");
        validUserList.add(user1);
        validUserList.add(user2);
        validUserList.add(user3);
        validUserList.add(user4);
        validUserList.add(user5);

        if (userService.getAllUsers().size() == 0) {
            for (User validUser : validUserList) {
                userService.postUser(validUser);
            }
        } else {
            userService.deleteAllUsers();
            for (User validUser : validUserList) {
                userService.postUser(validUser);
            }
        }
    }

    /**
     * Database is cleared and validUserList is set to null for garbage collection.
     * Any time a User is persisted to the database, the userId value changes
     * from null to the id given by database.
     * This is why we must empty the list and repopulate it after every test.
     */
    @AfterEach
    void deleteDBData() {
        userService.deleteAllUsers();
        validUserList = null;
    }

    /**
     * Test method checks that the Users persisted to database (size = 5)
     * is equal to the size of validUserList
     * (the list of Users that were persisted to database
     * in the setupDB method in this test class.)
     */
    @Test
    void getAllUsers() {
        // Method returns a list, so we use its size to verify users in database.
        assertEquals(validUserList.size(), userService.getAllUsers().size());
    }

    /**
     * Test method checks that we are able to retrieve all Users inside
     * validUserList using their userId as an argument.
     */
    @Test
    void getUserById() {
        // Loop to iterate over the validUserList initiated in setupDB method.
        // Remember, anytime a user is persisted, the userId is automatically
        // updated.
        for (User currentUser : validUserList) {
            // Method returns boolean, so we check for boolean value.
            assertTrue(userService.getUserById(currentUser.getUserId()).isPresent());
        }

        // Check to see if passing in a non-existent userId,
        // it should return false.
        assertFalse(userService.getUserById(0).isPresent());
    }

    /**
     * Test method checks that all Users in validUserList
     * can be deleted using their userId.
     */
    @Test
    void deleteUserById() {
        // loops through validUserList
        // using their userId to delete
        // them from database.
        for (User currentUser : validUserList) {
            // Method returns a boolean, so we check for a boolean value.
            assertTrue(userService.deleteUserById(currentUser.getUserId()));
        }

        // Check that a non-existent userId does not
        // yield a truthful result
        assertFalse(userService.deleteUserById(-1));
    }

    /**
     * Check to see if we are able to retrieve
     * all accounts that belong to the right user
     * using their userId
     */
    @Test
    void getAccountsByUserId() {
        // Since this method returns a list, we create
        // an account list to use as the expected value
        // and compare it to the actual value.
        List<Account> account = new LinkedList<>();

        // new account added to list.
        account.add(new Account("checking"));

        // setting account's user variable to a valid user
        account.get(0).setUser(validUserList.get(0));

        // Account is persisted to database
        accountService.postAccount(account.get(0), validUserList.get(0).getUserId());

        // Using the toString method to check that our account list
        // matches the output list.
        assertEquals(account.toString(),
                userService.getAccountsByUserId(validUserList.get(0).getUserId()).toString());

        // delete accounts since we can't delete users before accounts
        // due to foreign key constraints.
        accountService.deleteAllAccounts();
    }

    /**
     * Check that we can retrieve a User from the database
     * with a valid username and password.
     */
    @Test
    void getUserByUsername() {
        // We use the first valid user from our validUserList. The method only uses the
        // username and password for login so all other values are ignored.
        assertEquals(validUserList.get(0).toString(), userService.getUserByUsername(validUserList.get(0)).toString());

        // New user created to check for null username and password
        User nullValuesUser = new User();
        assertNull(userService.getUserByUsername(nullValuesUser));

        // New user created to check for only username being null.
        User nullUsername = new User();
        nullUsername.setPassword("fakePassword");
        assertNull(userService.getUserByUsername(nullUsername));

        // New user created to check for only password being null.
        User nullPassword = new User();
        nullUsername.setUsername("fakeUsername");
        assertNull(userService.getUserByUsername(nullPassword));
    }

    /**
     * Test are run in random order, because of this data
     * may be persisted to database when it should be.
     * We solved this issue by placing the post method test
     * inside a nested class.
     */
    @Nested
    class TestPostUser {
        // Initial values created, validUserList is overwritten
        @BeforeEach
        void addNewValidUserToList() {
            validUserList = new LinkedList<>();
            User newUser = new User("Louanne", "Platter", "p.platter@gmail.com", "abcd1234", "Pplatter123");
            validUserList.add(newUser);
        }

        // validUserList data is garbage collected, all users in database are deleted
        @AfterEach
        void deleteNewValidUserToList() {
            validUserList = null;
            userService.deleteAllUsers();
        }

        /**
         * This test checks if user is successfully persisted into database because it
         * has the right credentials and the correct format.
         * Make sure that the values here are not already used in the database.
         * The values that must be unique are:
         * - email
         * - username
         */
        @Test
        void postUser() throws QrGenerationException {
            // test that valid users are persisted to database, method returns boolean
            // which is what we used to detect success or failure.
            assertTrue(userService.postUser(validUserList.get(0)));

            // Following 2 loops check that wrongly formatted
            // users are not persisted
            for (User nullFieldUser : nullValueUserList) {
                assertFalse(userService.postUser(nullFieldUser));
            }

            for (User emptyStringField : emptyStringUserList) {
                assertFalse(userService.postUser(emptyStringField));
            }
        }
    }
}
