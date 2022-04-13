package com.revature.cachemoney.backend.beans.services;

import com.revature.cachemoney.backend.beans.models.Transfer;
import com.revature.cachemoney.backend.beans.repositories.AccountRepo;
import com.revature.cachemoney.backend.beans.repositories.TransferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TransferService {
    private TransferRepo transferRepo;
    private AccountRepo accountRepo;

    @Autowired
    public TransferService (TransferRepo transferRepo, AccountRepo accountRepo) {
        this.transferRepo = transferRepo;
        this.accountRepo = accountRepo;
    }

    public List<Transfer> findByDestinationUser(int userId) {
        return this.transferRepo.findByDestinationUser(userId);
    }

    public List<Transfer> findBySourceUser(int userId) {
        return this.transferRepo.findBySourceUser(userId);
    }

    public Transfer save(Transfer transfer, Integer userId) throws ResponseStatusException {
        int sourceId = transfer.getSourceAccount().getAccountId();
        transfer.setSourceAccount(accountRepo.getById(sourceId));
        if (transfer.getSourceAccount().getUser().getUserId() != userId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can only transfer money from YOUR accounts");
        }
        int destId = transfer.getDestinationAccount().getAccountId();
        try {
            transfer.setDestinationAccount(accountRepo.getById(destId));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to find destination account");
        }
        // Setting description to default
        if (transfer.getDescription() == null) {
            transfer.setDescription(
                    "Transfer from account "
                            + transfer.getSourceAccount().getAccountId()
                            + " to "
                            + transfer.getDestinationAccount().getAccountId()
            );
        }
        double amount = transfer.getAmount();
        double balance = transfer.getSourceAccount().getBalance();
        if (amount > 0 && balance >= amount) {
            transfer.getSourceAccount().setBalance(balance - amount);
            double destBalance = transfer.getDestinationAccount().getBalance();
            transfer.getDestinationAccount().setBalance(destBalance + amount);
            return this.transferRepo.save(transfer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough money to transfer specified amount");
        }
    }
}
