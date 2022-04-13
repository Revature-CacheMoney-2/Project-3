/**
 * @Author Cody Gonsowski, Jeffrey Lor
 */

import axios from "axios";
import React, { useEffect, useState } from "react";
import config from "../../config.js";
import store from "../../store/Store.js";
import CurrencyFormat from "react-currency-format";

function AccountList(props) {
	const doTitleUpdate = props.doTitleUpdate;
	// local transaction state
	const [accounts, setAccounts] = useState([]);
	// setAccounts is the setter function
	// read state with accounts

	const handleAccountUpdate = () => {
		const token = store.getState().userReducer.token;
		const userId = store.getState().userReducer.userId;

		const url = config.url;

		(token !== null) && (userId !== null) && axios
			.get(`${url}users/accounts`, {
				headers: {
					token,
					userId,
				},
			})
			.then((response) => {
				const allAccounts = response.data;
				setAccounts(allAccounts);
			})
			.catch((error) => console.error(`Error: ${error}`));
	};

	// Watch if someone did a deposit, etc
	//store.subscribe(handleAccountUpdate);

	// Caution, uncommenting this to fix accounts not updating may lead to infinite looping
	// Things attempted:
	// 1. Putting [accounts] in useEffect - it "works" but will call api infinitely until browser crashes
	// 2. Putting junk data (ex. currentDate in ms) to trigger update / track that as state - nope
	// 3. User store tracking... stuff... and did store.subscribe - mixed results, but I think infinite loop
	// 4. Separating out each account from accounts list into a new prop with details - didn't update at all
	// 5. Cursing out React / Javascript in general - no change in results
	// 6. Begging code to work - no noticeable change, may require a larger sacrifice
	// 7. Acceptance (current state) - this is a feature, not a bug
	// Good luck, future person.  May fortune be ever in your favor.

	// effect hook
	useEffect(() => {
		handleAccountUpdate();
	}, [props.doTitleUpdate]);

	//event, props, data, triggerEvent
	const handleAccountClick = (event) => {
		// if an account had previously been selected, hide the additional options
		const currentlySelectedAccount = accounts.filter((account) => {
			if (account.accountId === parseInt(event.currentTarget.id)) {
				return account;
			} else {
				return null;
			}
		})[0];

		store.dispatch({
			type: "UPDATE_CURRENT_ACCOUNT_ID",
			payload: event.currentTarget.id,
		});
		doTitleUpdate(currentlySelectedAccount);
		// Do an account update just because - hacky workaround to account state not tracking properly
		handleAccountUpdate();
	};

	// Populates a row (account) with information
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
							decimalScale={2}
							fixedDecimalScale={true}
							prefix={"$"}
						/>
					</div>
				</div>
			</div>
		);
	});

	// This displays a placeholder message when the user has no accounts.
	const noAccountsMessage = () => {
		if (accounts.length === 0) {
			return (
				<div className="no-account-message">
					You currently have no accounts. Select "Create Account" to get started
					today!
				</div>
			);
		} else {
			return;
		}
	};

	return (
		<>
			{noAccountsMessage()}
			<div className="account_list">{content}</div>
		</>
	);
}

export default AccountList;
