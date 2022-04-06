package com.revature.cachemoney.backend.beans.models;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model containing information regarding a Transaction.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private Integer transactionId;

	@JoinColumn(name = "account_id")
	@ManyToOne
	private Account account;

	@Column(name = "description")
	private String description;

	@Column(name = "transaction_date")
	private Date transactionDate;

	@Column(name = "transaction_amount")
	private Double transactionAmount;

	@Column(name = "ending_balance")
	private Double endingBalance;

	public Transaction(Account account, String description, Date transaction_date,
			Double transaction_amount, Double ending_balance) {
		this.account = account;
		this.description = description;
		this.transactionDate = transaction_date;
		this.transactionAmount = transaction_amount;
		this.endingBalance = ending_balance;
	}

	public Transaction(Account account, String description, Double transaction_amount, Double ending_balance) {
		this.account = account;
		this.description = description;
		this.transactionAmount = transaction_amount;
		this.endingBalance = ending_balance;
	}

	public Transaction(String description, Double transaction_amount, Double ending_balance) {
		this.description = description;
		this.transactionAmount = transaction_amount;
		this.endingBalance = ending_balance;
	}

	/**
	 *
	 * ******************STRICTLY FOR TESTING PURPOSES*********************
	 * @return this will return the toString method
	 * */
	public String toStringWithoutDate() {
		return "Transaction{" +
				"transactionId=" + transactionId +
				", account=" + account +
				", description='" + description + '\'' +
//				", transactionDate=" + transactionDate +
				", transactionAmount=" + transactionAmount +
				", endingBalance=" + endingBalance +
				'}';
	}
}
