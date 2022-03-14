/**
 * @Author Cody Gonsowski, Jeffrey Lor
 */

import axios from "axios";
import React, { useEffect, useState } from "react";
import config from "../../config.js";
import store from "../../store/Store.js";
import CurrencyFormat from "react-currency-format";
import "../../css/Account.css";

function AccountList(props) {
	// local transaction state
	const [accounts, getAccounts] = useState([]);

	// url
	const url = config.url;

	// effect hook
	useEffect(() => {
		getAllAccounts();
	});

	const getAllAccounts = () => {
		//${url}users/accounts/${store.getState().userId}
		axios
			.get(`${url}users/accounts`, {
				headers: {
					token: store.getState().userReducer.token,
					userId: store.getState().userReducer.userId,
				},
			})
			.then((response) => {
				const allAccounts = response.data;
				getAccounts(allAccounts);
			})
			.catch((error) => console.error(`Error: ${error}`));
	};

	// This occurs when the user has selected an account
	// it displays additional options (deposit, withdraw, transfer)
	const showAdditionalActions = () => {
		const selectedAccountId = store.getState().accountReducer.currentAccountId;
		// in theory, this should not work if it is an empty string
		console.log(selectedAccountId);
		if (selectedAccountId) {
			return (
				<div class="account-additional-options">
					<button class="account-option">Deposit</button>
					<button class="account-option">Withdrawal</button>
					<button class="account-option">Transfer</button>
				</div>
			);
		}
	};

	const handleAccountClick = (event, props, data, triggerEvent) => {
		// if an account had previously been selected, hide the additional options

		// TODO route to `Transaction` page
		store.dispatch({
			type: "UPDATE_CURRENT_ACCOUNT_ID",
			payload: event.currentTarget.id,
		});
		//showAdditionalActions();

		// how to access the associated account id
		//console.log("store: ", store.getState().accountReducer.currentAccountId);
	};

	const content = accounts.map((account) => {
		return (
			<div
				className="account_item"
				key={account.accountId}
				id={account.accountId}
				onClick={handleAccountClick}
			>
				<div className="account_name">
					<p>
						{account.name} (***{account.accountId.toString().slice(-4)})
					</p>
				</div>
				<div className="account_item_info">
					<div className="account_type">
						<p>{account.type}</p>
					</div>
					<div className="account_balance">
						<CurrencyFormat
							value={account.balance}
							displayType={"text"}
							thousandSeparator={true}
							prefix={"$"}
						/>
					</div>
				</div>
			</div>
		);
	});

	return (
		<>
			<div className="account_list">{content}</div>
		</>
	);
}

export default AccountList;
