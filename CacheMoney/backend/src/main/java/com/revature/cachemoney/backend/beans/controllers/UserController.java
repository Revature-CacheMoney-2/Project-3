package com.revature.cachemoney.backend.beans.controllers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.annotations.RequireJwt;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.security.JwtUtil;
import com.revature.cachemoney.backend.beans.security.payload.MfaRequest;
import com.revature.cachemoney.backend.beans.security.payload.MfaResponse;
import com.revature.cachemoney.backend.beans.services.UserService;

import dev.samstevens.totp.exceptions.QrGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle requests related to Users.
 * 
 * @author Alvin Frierson, Brian Gardner, Cody Gonsowski, and Jeffrey Lor
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
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
    @GetMapping(value = "/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * GET the User with provided ID.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return User object
     * @throws JsonProcessingException this is thrown when there is an issue with the JSON string
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
     * @param user containing the firstName, lastName, email, username, password, & mfa
     * @return MfaResponse | badRequest() based on registration status
     * @throws JsonProcessingException If any error occur in the Json process
     * @throws QrGenerationException If any error occur in the Qr Image Generator in the 2fa authentication
     */
    @PostMapping
    public ResponseEntity<String> postUser(@RequestBody User user)
            throws JsonProcessingException, QrGenerationException {

        if(userService.postUser(user)){

            return ResponseEntity.ok().body(mapper.writeValueAsString(
                    new MfaResponse(user.isMfa(), user.getQrImageUri())
            ));
        }

        // indicate bad request
        return ResponseEntity.badRequest().build();
    }

    /**
     * POST a User for update the mfa flag.
     *
     * @param token  for current session
     * @param userId for current User
     * @param mfa  new value for mfa flag
     * @return MfaResponse based on update status
     * @throws JsonProcessingException If any error occur in the Json process
     * @throws QrGenerationException If any error occur in the Qr Image Generator in the 2fa authentication
     */
    @PostMapping(value = "/2fa")
    @RequireJwt
    public ResponseEntity<String> update2faUser(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId,
            @RequestParam Boolean mfa) throws JsonProcessingException, QrGenerationException {

        return ResponseEntity.ok().body(mapper.writeValueAsString(userService.update2faUser(userId, mfa)));
    }

    /**
     * DELETE a User with provided ID.
     * Returns a bad request if the DELETE is unsuccessful.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return OK | Bad Request based on DELETE success
     */
    @DeleteMapping
    @RequireJwt
    public ResponseEntity<String> deleteUserById(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId) {

        userService.deleteUserById(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Log in to a User account.
     * 
     * @param user containing (at least) username and password
     * @return User object and its associated JWT
     * @throws JsonProcessingException this is thrown when there is an issue with the JSON string
     */
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody User user) throws JsonProcessingException {
        // has internal checking to see if user is valid
        User tempUser = userService.getUserByUsername(user);

        // make sure the user is valid
        if (tempUser != null) {
            if(tempUser.isMfa()){
                return ResponseEntity.ok().body(mapper.writeValueAsString(tempUser));
            }
            else {
                // write the headers & object into the response
                return ResponseEntity.ok()
                        .headers(this.generateToken(tempUser.getUserId()))
                        .body(mapper.writeValueAsString(tempUser));
            }
        }

        // indicate bad request
        return ResponseEntity.badRequest().build();
    }

    /**
    * Verify the TOTP for 2FA process.
    *
    * @param request With the Id of the user and TOPT code to verify
    * @return UserID & its associated JWT
    */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody MfaRequest request) {

        Integer userId = request.getUserId();
        String code = request.getCode();
        if(userService.verify(userId, code)){
            // write the headers & object into the response
            return ResponseEntity.ok().headers(this.generateToken(userId)).body(userId.toString());
        }

        // indicate bad request
        return ResponseEntity.badRequest().body("The verification process failed: Invalid TOTP code");
    }

    /**
     * GET all Accounts associated with a particular User ID.
     * 
     * @param token  for current session
     * @param userId for current User
     * @return List of Accounts associated with a particular user
     * @throws JsonProcessingException this is thrown when there is an issue with the JSON string
     */
    @GetMapping(value = "/accounts")
    @RequireJwt
    public ResponseEntity<String> getAccountsByUserId(
            @RequestHeader(name = "token") String token,
            @RequestHeader(name = "userId") Integer userId)
            throws JsonProcessingException {

        return ResponseEntity.ok().body(mapper.writeValueAsString(userService.getAccountsByUserId(userId)));
    }

    private HttpHeaders generateToken(Integer userId){
        HttpHeaders headers = new HttpHeaders();

        // add the JWT to the headers
        headers.set("JWT", jwtUtil.generateToken(userId));
        headers.set("Access-Control-Expose-Headers", "JWT");

        return headers;
    }
}
