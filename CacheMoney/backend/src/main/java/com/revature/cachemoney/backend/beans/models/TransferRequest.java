package com.revature.cachemoney.backend.beans.models;

import lombok.*;

import javax.persistence.*;

/**
 * Model containing information regarding a Request for a Transfer.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transfer_request")
public class TransferRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer requestId;

    @OneToOne
    private Account sourceAccount;

    @OneToOne
    private Account destinationAccount;

    @Column
    private double amount;

    @Column
    private String description;

    public Transfer toTransfer() {
        Transfer transfer = new Transfer();
        transfer.setDestinationAccount(this.getDestinationAccount());
        transfer.setSourceAccount(this.getSourceAccount());
        transfer.setAmount(this.getAmount());
        transfer.setDescription(this.getDescription());
        System.out.println(transfer.toString());
        return transfer;
    }
}