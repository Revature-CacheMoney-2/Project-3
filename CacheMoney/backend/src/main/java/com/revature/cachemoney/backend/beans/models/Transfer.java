package com.revature.cachemoney.backend.beans.models;

import lombok.*;

import javax.persistence.*;

/**
 * Model containing information regarding a Transfer.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer transferId;

    @OneToOne
    private Account sourceAccount;

    @OneToOne
    private Account destinationAccount;

    @Column
    private double amount;

    @Column
    private String description;
}
