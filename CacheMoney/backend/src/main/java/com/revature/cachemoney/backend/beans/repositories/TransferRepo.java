package com.revature.cachemoney.backend.beans.repositories;


import com.revature.cachemoney.backend.beans.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface TransferRepo extends JpaRepository<Transfer, Integer> {

    @Query("FROM Transfer t WHERE t.destinationAccount.user.userId = :userId")
    public List<Transfer> findByDestinationUser(int userId);

    @Query("FROM Transfer t WHERE t.sourceAccount.user.userId = :userId")
    public List<Transfer> findBySourceUser(int userId);

    public Transfer save(Transfer transfer);
}
