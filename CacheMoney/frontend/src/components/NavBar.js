import React from "react";

function NavBar(props) {
	// Make sure to add additional cases to MainPageView to handle any routing changes/new links
	return (
		<div className="navigation-bar">
			<span
				className="navigation-link"
				onClick={props.handleClick}
				id="account-overview"
			>
				Accounts
			</span>

			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="create-account"
				>
					Open Account
				</span>
			</a>
			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="investment-portfolio"
				>
					Investment Porfolio
				</span>
			</a>
			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="external-transfer"
				>
					Send Money
				</span>
			</a>
		</div>
	);
}
export default NavBar;