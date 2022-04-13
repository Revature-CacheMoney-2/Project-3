package com.revature.cachemoney.backend.beans.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.security.payload.MfaResponse;
import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.security.JwtUtil;
import com.revature.cachemoney.backend.beans.services.UserService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Unit testing of the UserController class.
 * 
 * @author David Alvarado, Brandon Perrien,
 *         Jeremiah Smith, Alvin Frierson,
 *         Trevor Hughes, Maja Wirkijowska,
 *         Ahmad Rawashdeh, Ibrahima Diallo,
 *         Brian Gardner, Jeffrey Lor,
 *         Mark Young
 */
// allows us to mock HttpRequests
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private final UserController userController = new UserController(userService, jwtUtil, mapper);

    @Autowired
    private MockMvc mockMvc;

    private User[] testUsers;
    private String[] testTokens;
    private User[] loginUsers;
    private List<Account> accountList;

    /**
     * Data initialization before each test method.
     */
    @BeforeEach
    void populateData() {
        // create users for testing
        testUsers = new User[2];
        testUsers[0] = new User("Hank", "Hill", "hank.hill@gmail.com", "abcd1234", "hankhill");
        testUsers[0].setMfa(true);
        testUsers[0].setQrImageUri("https://qrImagen.com/image.png");

        testUsers[1] = new User("Peggy", "Hill", "peggy.hill@gmail.com", "efgh5678", "peggyhill");

        // create two tokens for testing
        testTokens = new String[2];
        testTokens[0] = "hankToken";
        testTokens[1] = "peggyToken";

        // users for attempted login
        loginUsers = new User[3];

        // valid username and password for hank in testUsers[0]
        loginUsers[0] = new User(null, null, null, "abcd1234", "hankhill");

        // invalid username for peggy in testUsers[1]
        loginUsers[1] = new User(null, null, null, "efgh5678", "peggy123");

        // invalid password for hank in testUsers[0]
        loginUsers[2] = new User(null, null, null, "peggy", "hankhill");

        // new account list
        accountList = new LinkedList<>();
        accountList.add(new Account("checking"));
        accountList.add(new Account("savings", "Rainy day fund"));
    }

    /**
     * Data deleted after each method in case changes were made.
     */
    @AfterEach
    void depopulateData() {
        testUsers = null;
        testTokens = null;
        accountList = null;
    }

    /**
     * Testing the ability to retrieve a User with a valid userId.
     */
    @Test
    void getUserById() throws Exception {
        // setting userId to a valid int.
        testUsers[0].setUserId(1);

        // Mocking the return value of the userService called by this controller.
        when(userService.getUserById(testUsers[0].getUserId())).thenReturn(Optional.of(testUsers[0]));

        // Mocking http request with the required headers and checking that the first
        // name matches what the mocked service returns
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/users")
                .header("token", testTokens[0])
                .header("userId", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(".firstName").value(testUsers[0].getFirstName()));
    }

    /**
     * Testing the login controller.
     * (Endpoint was not used to test this method)
     */
    @Test
    void login() throws JsonProcessingException {
        // user id set
        testUsers[0].setUserId(1);

        // mocking userService to return the first user in testUsers
        when(userService.getUserByUsername(loginUsers[0])).thenReturn(testUsers[0]);

        // mocking jwt to return the first token in testTokens
        when(jwtUtil.generateToken(testUsers[0].getUserId())).thenReturn(testTokens[0]);

        // Creating a response entity to compare individual values
        ResponseEntity<String> response = userController.login(loginUsers[0]);

        // Checking that the response body matches the first user in testUsers
        assertEquals(mapper.writeValueAsString(testUsers[0]), response.getBody());

        // Checking that the response code is ok
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Checking that the JWT header which contains a token value matches our
        // first test token from testTokens
        assertEquals(testTokens[0], response.getHeaders().getFirst("JWT"));

        // testing incorrect username
        // mocking a userService return value of null which is expected for
        // an invalid credential.
        when(userService.getUserByUsername(loginUsers[1])).thenReturn(null);
        ResponseEntity<String> response1 = userController.login(loginUsers[1]);
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        // testing incorrect password
        // testing incorrect username
        // mocking a userService return value of null which is expected for
        // an invalid credential.
        when(userService.getUserByUsername(loginUsers[2])).thenReturn(null);
        ResponseEntity<String> response2 = userController.login(loginUsers[2]);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    /**
     * Method test checks that we can retrieve all Accounts
     * owned by the same User using the userId.
     */
    @Test
    void getAccountsByUserId() throws JsonProcessingException {
        // set user id to 1
        testUsers[0].setUserId(1);

        // mocking the userService to return our accountList
        when(userService.getAccountsByUserId(testUsers[0].getUserId())).thenReturn(accountList);
        ResponseEntity<String> response = userController.getAccountsByUserId(testTokens[0], 1);

        // Checking that our list and the response body returned by controller match
        assertEquals(mapper.writeValueAsString(accountList), response.getBody());

        // checking that the http status is ok.
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Method test to check that we are able
     * to register a User (and post to database).
     */
    @Test
    void postUser() throws Exception {
        // mocking that the user was successfully persisted to database
        when(userService.postUser(testUsers[0])).thenReturn(true);


        // checking that the returned by controller method is true
        ResponseEntity<String> response1 = userController.postUser(testUsers[0]);
        MfaResponse objResponse1 = mapper.readValue(response1.getBody(), MfaResponse.class);
        assertEquals(objResponse1.getSecretImageUri(), testUsers[0].getQrImageUri());
        assertEquals(objResponse1.isMfa(), testUsers[0].isMfa());

        // mocking that the user was unsuccessfully persisted to database
        when(userService.postUser(loginUsers[0])).thenReturn(false);

        // checking that the returned by controller method is false
        ResponseEntity<String> response2 = userController.postUser(loginUsers[0]);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

        // mocking that the user was successfully persisted to database
        when(userService.postUser(testUsers[0])).thenReturn(true);

        // writing our testUser as a JSON string to pass into the method as part
        // of the request body
        String jsonString = mapper.writeValueAsString(testUsers[0]);

        // mock http POST request made status code is checked
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
                .andExpect(status().isOk());
    }
}
