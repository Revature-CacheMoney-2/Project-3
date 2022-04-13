package com.revature.cachemoney.backend.beans.services;

import com.revature.cachemoney.backend.beans.models.Account;
import com.revature.cachemoney.backend.beans.models.Transfer;
import com.revature.cachemoney.backend.beans.models.TransferRequest;
import com.revature.cachemoney.backend.beans.models.User;
import com.revature.cachemoney.backend.beans.repositories.AccountRepo;
import com.revature.cachemoney.backend.beans.repositories.TransferRepo;
import com.revature.cachemoney.backend.beans.repositories.TransferRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransferRequestService {
    TransferRequestRepo transferRequestRepo;
    TransferRepo transferRepo;
    AccountRepo accountRepo;
    TransferService transferService;

    @Autowired
    public TransferRequestService(TransferRequestRepo transferRequestRepo, TransferRepo transferRepo, AccountRepo accountRepo, TransferService transferService) {
        this.transferRequestRepo = transferRequestRepo;
        this.transferRepo = transferRepo;
        this.accountRepo = accountRepo;
        this.transferService = transferService;
    }

    public TransferRequest save(TransferRequest transferRequest) throws ResponseStatusException {
        Account destinationAccount = accountRepo.getById(transferRequest.getDestinationAccount().getAccountId());
        Account sourceAccount = accountRepo.getById(transferRequest.getSourceAccount().getAccountId());
        // check if amount is > 0
        if(transferRequest.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount cannot be less than or equal 0");
        }
        User destinationUser = destinationAccount.getUser();
        User sourceUser = sourceAccount.getUser();
        // check if destination is different from source
        if (destinationUser != sourceUser) {
            if (destinationAccount != sourceAccount) {
                // check if destination and source exists
                if (destinationAccount != null && sourceAccount != null) {
                    transferRequest.setDestinationAccount(destinationAccount);
                    transferRequest.setSourceAccount(sourceAccount);
                    // Setting Description to default
                    if (transferRequest.getDescription() == null) {
                        transferRequest.setDescription(
                                "Transfer from account "
                                        + transferRequest.getSourceAccount().getAccountId()
                                        + " to "
                                        + transferRequest.getDestinationAccount().getAccountId()
                        );
                    }
                    return this.transferRequestRepo.save(transferRequest);
                }
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source and Destination accounts must exist");
            }
            // if source and/or destination is the same or null
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source and Destination accounts cannot be the same account");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't request money from yourself, just do it in a transfer");
    }

    public List<TransferRequest> findByRequestedUser(int userId) {
        return this.transferRequestRepo.findByRequestedUser(userId);
    }

    public List<TransferRequest> findByRequestingUser(int userId) {
        return this.transferRequestRepo.findByRequestingUser(userId);
    }

    public void delete(int requestId, Integer userId) throws ResponseStatusException {
        TransferRequest transferRequest = this.transferRequestRepo.findById(requestId);
        int destinationUser = transferRequest.getDestinationAccount().getUser().getUserId();
        int sourceUser = transferRequest.getSourceAccount().getUser().getUserId();
        // only involved users can delete the request
        if(destinationUser == userId || sourceUser == userId) {
            this.transferRequestRepo.delete(transferRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only the requester or requestee may delete a request");
        }
    }

    public Transfer acceptTransfer(int requestId, Integer userId) throws ResponseStatusException {
        TransferRequest transferRequest = this.transferRequestRepo.findById(requestId);
        Transfer returnTransfer = this.transferService.save(transferRequest.toTransfer(), userId);
        if(returnTransfer!=null) {
            delete(transferRequest.getRequestId(), userId);
            return returnTransfer;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer was not saved");
    }
}
