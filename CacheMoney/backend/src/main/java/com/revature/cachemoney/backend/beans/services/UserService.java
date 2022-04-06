package com.revature.cachemoney.backend.beans.services;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.repositories.AccountRepo;
import com.revature.cachemoney.backend.beans.repositories.UserRepo;
import com.revature.cachemoney.backend.beans.security.SecurityConfig;

import com.revature.cachemoney.backend.beans.utils.EmailUtil;
import com.revature.cachemoney.backend.beans.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

/**
 * Service layer for User requests.
 * 
 * @author Alvin Frierson
 */
@Service
public class UserService {
    private final UserRepo userRepo;
    private final AccountRepo accountRepo;

    private final SecurityConfig passwordEncoder;

    private final String emailRegEx = "^[a-zA-Z0-9._-]+@{1}[a-zA-Z0-9-_]+[.]{1}[a-zA-Z0-9]+[a-zA-Z_.-]*$";
    private final String nameRegEx = "^[a-zA-Z][a-zA-Z' -]+[a-zA-Z]$";
    private final String usernameRegEx = "^[a-zA-Z0-9@~._-]{8,}$";
    private final String passwordRegEx = "^[a-zA-Z0-9@^%$#/\\,;|~._-]{8,50}$";

    @Autowired
    public UserService(UserRepo userRepo, AccountRepo accountRepo, SecurityConfig passwordEncoder) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Service method to GET *ALL* Users.
     * 
     * @return List of Users
     */
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Service method to GET a User by ID.
     * 
     * @param userId of User to find
     * @return User
     */
    public Optional<User> getUserById(Integer userId) {
        return userRepo.findById(userId);
    }

    /**
     * Service method to POST a User.
     * 
     * @param user of User to save
     * @return (true | false) if the User is saved
     */
    public Boolean postUser(User user) {
        // verify the User's credentials
        if (areCredentialsValid(user)) {
            try {
                // encodes the password for database storage
                user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));

                // changing to lowercase so that two usernames that are the same with different cases won't both be accepted
                user.setEmail(user.getEmail().toLowerCase());
                user.setUsername(user.getUsername().toLowerCase());

                // save the user in the database
                userRepo.save(user);
            } catch (Exception e) {
                // fail on save exception
                return false;
            }

            // inform successful result
            try {
                String body = PropertiesUtil.getHTML("src/main/resources/welcome.html");
                body = body.replace("{FIRSTNAME LASTNAME}", user.getFirstName() + " " + user.getLastName());
                EmailUtil.getInstance().sendEmail(user.getEmail(), "Account Created", body);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        }

        // fail by default
        return false;
    }

    // DELETE a user by ID
    public Boolean deleteUserById(Integer id) {
        try{
            userRepo.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    /**
     * Service method to GET a User by username.
     * 
     * @param user containing at least username and password
     * @return User object with username
     */
    public User getUserByUsername(User user) {
        // fail if the username or password is null
        if (user.getUsername() == null || user.getPassword() == null) {
            return null;
        }

        // verify the username matches
        ExampleMatcher em = ExampleMatcher.matching()
                .withIgnorePaths("user_id", "first_name", "last_name", "email", "accounts", "password")
                .withMatcher("username", ignoreCase());

        // search for the User in the database
        Example<User> example = Example.of(user, em);

        // does the User exist?
        if (userRepo.exists(example)) {
            // get the actual User
            Optional<User> optionalUser = userRepo.findOne(example);

            // password checking
            if (passwordEncoder.passwordEncoder().matches(user.getPassword(), optionalUser.get().getPassword())) {
                return optionalUser.get();
            }
        }

        return null;
    }

    /**
     * Service method to GET Accounts associated with a User's ID
     * 
     * @param userId of associated User
     * @return List of Accounts associated with User's ID
     */
    public List<Account> getAccountsByUserId(Integer userId) {
        return accountRepo.findByUser(userRepo.getById(userId));
    }

    /**
     * Helper function to validate login credentials.
     * 
     * @param user to check for valid credentials
     * @return (true | false) based on login status
     */
    public Boolean areCredentialsValid(User user) {
        // fail if any fields are null
        if (user.getFirstName() == null || user.getLastName() == null ||
                user.getEmail() == null || user.getUsername() == null ||
                user.getPassword() == null) {
            return false;
        }

        // fail if first name & last name are empty
        if (user.getFirstName().isEmpty() || user.getLastName().isEmpty()) {
            return false;
        }

        // validify email
        Pattern emailPattern = Pattern.compile(emailRegEx);
        Matcher emailMatcher = emailPattern.matcher(user.getEmail());
        Boolean emailValidity = emailMatcher.matches();

        // validify first name & last name
        Pattern namePattern = Pattern.compile(nameRegEx);
        Matcher nameMatcher = namePattern.matcher(user.getFirstName() + " " + user.getLastName());
        Boolean nameValidity = nameMatcher.matches();

        // validify username
        Pattern usernamePattern = Pattern.compile(usernameRegEx);
        Matcher usernameMatcher = usernamePattern.matcher(user.getUsername());
        Boolean usernameValidity = usernameMatcher.matches();

        // validify password
        Pattern passwordPattern = Pattern.compile(passwordRegEx);
        Matcher passwordMatcher = passwordPattern.matcher(user.getPassword());
        Boolean passwordValidity = passwordMatcher.matches();

        // succeed only if all fields are valid
        return emailValidity && nameValidity && usernameValidity && passwordValidity;
    }

    /**
     *
     * ******************STRICTLY FOR TESTING PURPOSES*********************
     *
     */
    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}
