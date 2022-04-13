package com.revature.cachemoney.backend.beans.repositories;

import java.util.List;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is a repository to handle data persistence CRUD tied to an Account.
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    /**
     * FIND some number of Accounts associated with a given User.
     * Only need to provide the User's ID in the object.
     * 
     * @param user to find
     * @return List of Accounts
     */
    List<Account> findByUser(User user);
}
