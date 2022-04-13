/**
 * @Author Cody Gonsowski, Jeffrey Lor
 */

import { useEffect, useState } from "react";
import axios from "axios";
import store from "../../store/Store";
import config from "../../config.js";
import CurrencyFormat from "react-currency-format";

function TransactionTable(props) {
	// local transaction state
	const [transactions, setTransactions] = useState([]);

	// url
	const url = config.url;

	// default props
	TransactionTable.defaultProps = {
		/**
		 * Expected values:
		 *   NONE
		 *   CREDIT
		 *   DEBIT
		 */
		filter: "NONE",
	};

	// effect hook
	useEffect(() => {
		getAllTransactions();
	}, []);

	// retrieve all transactions based on user's account's id
	const getAllTransactions = () => {
		axios
			.post(
				`${url}accounts/transactions`,
				store.getState().accountReducer.currentAccountId,
				{
					headers: {
						token: store.getState().userReducer.token,
						userId: store.getState().userReducer.userId,
						"Content-Type": "application/json; charset=utf-8",
					},
				}
			)
			.then((response) => {
				const allTransactions = response.data;
				setTransactions(allTransactions);
			})
			.catch((error) => console.error(`Error: ${error}`));
	};

	// map transactions from local state based on store
	const content = transactions
		.filter((transaction) => {
			if (props.filter === "NONE") {
				return true;
			} else if (props.filter === "CREDIT") {
				return transaction.transactionAmount > 0;
			} else if (props.filter === "DEBIT") {
				return transaction.transactionAmount < 0;
			} else {
				console.error("Invalid amount filter!");
				return false;
			}
		})
		.map((transaction) => {
			return (
				<tr key={transaction.transactionId}>
					<td>{transaction.transactionDate}</td>
					<td>{transaction.description}</td>
					<td>
						<CurrencyFormat
							className={
								transaction.transactionAmount >= 0
									? "positive_balance"
									: "negative_balance"
							}
							value={transaction.transactionAmount}
							displayType={"text"}
							thousandSeparator={true}
							decimalScale={2}
							fixedDecimalScale={true}
							prefix={"$"}
						/>
					</td>
					<td>
						<CurrencyFormat
							value={transaction.endingBalance}
							displayType={"text"}
							thousandSeparator={true}
							decimalScale={2}
							fixedDecimalScale={true}
							prefix={"$"}
						/>
					</td>
				</tr>
			);
		});

	// the html stuff
	return (
		<>
			<div className="transaction-container" id="transaction-list">
				<table className="transaction_table">
					<thead>
						<tr>
							<th id="header_date">Date</th>
							<th id="header_description">Description</th>
							<th id="header_amount">Credit/Debit</th>
							<th id="header_balance">Balance</th>
						</tr>
					</thead>
					<tbody>{content}</tbody>
				</table>
			</div>
		</>
	);
}

export default TransactionTable;
