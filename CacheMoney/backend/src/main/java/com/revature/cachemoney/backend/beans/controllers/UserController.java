package com.revature.cachemoney.backend.beans.controllers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.annotations.RequireJwt;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.security.JwtUtil;
import com.revature.cachemoney.backend.beans.services.UserService;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle requests related to Users.
 * 
 * @author Alvin Frierson, Brian Gardner, Cody Gonsowski, & Jeffrey Lor
 */
@CrossOrigin
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, ObjectMapper mapper) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

	/**
	 * GET *ALL* Users.
	 * 
	 * @return List of all Users
	 */
    @GetMapping(value = "all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * GET the User with provided ID.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return User object
     * @throws JsonProcessingException
     */
    @GetMapping
    @RequireJwt
    public ResponseEntity<String> getUserById(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId)
            throws JsonProcessingException {

        return ResponseEntity.ok().body(mapper.writeValueAsString(userService.getUserById(userId)));
    }

    /**
     * POST a User.
     * 
     * @param user containing the firstName, lastName, email, username, & password
     * @return true | false based on registration status
     */
    @PostMapping
    public Boolean postUser(@RequestBody User user) {
        return userService.postUser(user);
    }

    /**
     *  Updates the provided fields of the user. Requires the oldPassword param to update password.
     * @param token The JSON Web Token for the current session.
     * @param userId The id of the user to update
     * @param oldPassword The old password if updating the password to verify that they know the old password.
     * @param user The user object that will only have the fields to be update being set. The rest will be null.
     * @return Whether the update was successful or not.
     */
    @PatchMapping
    @RequireJwt
    public ResponseEntity<String> updateUser(@RequestHeader(name= "token") String token, @RequestHeader(name="userId") Integer userId,
                                             @RequestHeader(name="oldPassword", required = false) String oldPassword, @RequestBody User user) throws JsonProcessingException {
        user.setUserId(userId);
        boolean result = userService.patchUser(user, oldPassword);
        return ResponseEntity.ok().body(mapper.writeValueAsString(result));

    }

    /**
     * DELETE a User with provided ID.
     * Returns a bad request if the DELETE is unsuccessful.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return OK | Bad Request based on DELETE success
     * @throws JsonProcessingException
     */
    @DeleteMapping
    @RequireJwt
    public ResponseEntity<String> deleteUserById(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId)
            throws JsonProcessingException {

        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Log in to a User account.
     * 
     * @param user containing (at least) username & password
     * @return User object & its associated JWT
     * @throws JsonProcessingException
     */
    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody User user) throws JsonProcessingException {
        // has internal checking to see if user is valid
        User tempUser = userService.getUserByUsername(user);

        // make sure the user is valid
        if (tempUser != null) {
            // create the response headers
            HttpHeaders headers = new HttpHeaders();

            // add the JWT to the headers
            headers.set("JWT", jwtUtil.generateToken(tempUser.getUserId()));
            headers.set("Access-Control-Expose-Headers", "JWT");

            // write the headers & object into the response
            return ResponseEntity.ok().headers(headers).body(mapper.writeValueAsString(tempUser));
        }

        // indicate bad request
        return ResponseEntity.badRequest().build();
    }

    /**
     * GET all Accounts associated with a particular User ID.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return List of Accounts associated with a particular user
     * @throws JsonProcessingException
     */
    @GetMapping(value = "/accounts")
    @RequireJwt
    public ResponseEntity<String> getAccountsByUserId(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId)
            throws JsonProcessingException {

        return ResponseEntity.ok().body(mapper.writeValueAsString(userService.getAccountsByUserId(userId)));
    }



    // PATCH ATTEMPT, for future Cohorts
   /* @PatchMapping("/profile/{resetpassword}")
    public ResponseEntity<User> updateUserPartially(
            @PathVariable(value = "id") Long userId,
            @RequestBody User userDetails) throws Throwable {
        SimpleJpaRepository userRepository = null;
        User user = (User) userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: "+ userId));

        user.setPassword(userDetails.getPassword());
        user.setUpdatedAt();
        final User updatedUser = (User) userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    } */
}
