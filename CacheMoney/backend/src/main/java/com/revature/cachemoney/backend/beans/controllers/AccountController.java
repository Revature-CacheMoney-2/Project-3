package com.revature.cachemoney.backend.beans.controllers;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.cachemoney.backend.beans.annotations.RequireJwt;
import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transaction;
import com.revature.cachemoney.backend.beans.models.Transfer;
import com.revature.cachemoney.backend.beans.services.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This is a controller to handle requests related to Accounts.
 * 
 * @author Alvin Frierson, Brian Gardner, Cody Gonsowski, and Jeffrey Lor
 */
@CrossOrigin
@RestController
@RequestMapping("/accounts")
public class AccountController {
	private final AccountService accountService;
	private final ObjectMapper mapper;

	@Autowired
	public AccountController(AccountService accountService, ObjectMapper mapper) {
		this.accountService = accountService;
		this.mapper = mapper;
	}

	/**
	 * GET *ALL* Accounts.
	 * 
	 * @return List of all Accounts
	 */
	@GetMapping(value = "/all")
	public List<Account> getAllAccounts() {
		return accountService.getAllAccounts();
	}

	/**
	 * GET the Account with provided ID of the associated User.
	 * Returns a bad request if the Account is not associated with the User.
	 * 
	 * @param token     for current session
	 * @param userId    for current User
	 * @param accountId for User's Account
	 * @return Account associated with the User
	 * @throws JsonProcessingException this is thrown when an issue arises from the JSON string
	 */
	@GetMapping
	@RequireJwt
	public ResponseEntity<String> getAccountByID(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Integer accountId)
			throws JsonProcessingException {

		// retrieve account
		Optional<Account> account = accountService.getAccountByID(accountId, userId);

		// see if an account was actually retrieved
		if (account.isPresent()) {
			return ResponseEntity.ok().body(mapper.writeValueAsString(account.get()));
		}

		// return bad request when account is not retrieved successfully
		return ResponseEntity.badRequest().build();
	}

	/**
	 * POST an Account with provided ID.
	 * Returns a bad request if the POST is unsuccessful.
	 * 
	 * @param token   for current session
	 * @param userId  for current User
	 * @param account for User's Account
	 * @return OK | Bad Request based on POST success
	 */
	@PostMapping
	@RequireJwt
	public ResponseEntity<String> postAccount(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Account account) {

		if (accountService.postAccount(account, userId)) {

			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}

	/**
	 * DELETE an Account with provided ID.
	 * Returns a bad request if the DELETE is unsuccessful.
	 * 
	 * @param token     for current session
	 * @param userId    for current User
	 * @param accountId for User's Account
	 * @return OK | Bad Request based on DELETE success
	 */
	@DeleteMapping
	@RequireJwt
	public ResponseEntity<String> deleteAccountById(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Integer accountId) {

		if (accountService.deleteAccountById(accountId, userId)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}

	/**
	 * POST (GET) all transactions associated with an Account.
	 * 
	 * @param token     for current session
	 * @param userId    for current User
	 * @param accountId for User's Account
	 * @return List of Transactions associated with a particular User's Account
	 * @throws JsonProcessingException this is thrown when there is an issue with the JSON string
	 */
	@PostMapping(value = "/transactions")
	@RequireJwt
	public ResponseEntity<String> getTransactionsById(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Integer accountId)
			throws JsonProcessingException {

		return ResponseEntity.ok()
				.body(mapper.writeValueAsString(accountService.getTransactionsById(accountId, userId)));
	}

	/**
	 * POST a deposit to an Account.
	 * Returns a bad request if the POST is unsuccessful.
	 * 
	 * @param token       for current session
	 * @param userId      for current User
	 * @param transaction for User's Transaction
	 * @return OK | Bad Request based on POST success
	 */
	@PostMapping(value = "/deposit")
	@RequireJwt
	public ResponseEntity<String> deposit(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Transaction transaction) {

		if (accountService.depositToAccount(userId, transaction)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}

	/**
	 * POST a withdrawl to an Account.
	 * Returns a bad request if the POST is unsuccessful.
	 * 
	 * @param token       for current session
	 * @param userId      for current User
	 * @param transaction for User's Transaction
	 * @return OK | Bad Request based on POST success
	 */
	@PostMapping(value = "/withdraw")
	@RequireJwt
	public ResponseEntity<String> withdraw(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Transaction transaction) {

		if (accountService.withdrawFromAccount(userId, transaction)) {
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}

	/**
	 * POST a withdrawl to an Account.
	 * Returns a bad request if the POST is unsuccessful.
	 * 
	 * @param token       for current session
	 * @param userId      for current User
	 * @param transfer for User's Transaction
	 * @return OK | Bad Request based on POST success
	 */
	@PostMapping(value = "/transfer")
	@RequireJwt
	public ResponseEntity<String> transfer(
			@RequestHeader(name = "token") String token,
			@RequestHeader(name = "userId") Integer userId,
			@RequestBody Transfer transfer) {

		if (accountService.transferBetweenAccountsOfOneUser(userId, transfer.getSourceAccountId(),
				transfer.getDestinationAccountId(), transfer.getTransaction())) {

			return ResponseEntity.ok().build();
		}

		return ResponseEntity.badRequest().build();
	}
}
