import React, { useState } from "react";
import Footer from "./Footer.js";
import userStore from "../store/Store.js";
import NavBar from "./NavBar.js";
import { useNavigate } from "react-router-dom";
import { useDarkMode } from "./style/useDarkMode";
import { ThemeProvider } from "styled-components";
import { GlobalStyles } from "./style/GlobalStyles";
import Toggle from "./style/Toggle";
import { lightTheme, darkTheme } from "./style/Themes";
import CreateAccount from "./Account/CreateAccount.js";
import AccountDisplay from "./Account/AccountDisplay.js";

function MainPageView2() {
	const navigate = useNavigate();
	let userData = userStore.getState().userReducer;

	const [page, setPage] = useState("account-overview");

	const handleLogout = (event) => {
		userStore.dispatch({
			type: "LOGOUT_USER",
			payload: "",
		});

		navigate("/");
	};

	const toProfile = (event) => {

		navigate("/profile");
	};

	const [theme, themeToggler, mountedComponent] = useDarkMode();
	const themeMode = theme === "light" ? lightTheme : darkTheme;

	if (!mountedComponent) return <div />;

	const updateMainPageContent = (event) => {
		setPage(event.target.id);
		//mainPageContentComponent(event.target.id);
		mainPageContentComponent(page);
	};

	const mainPageContentComponent = () => {
		switch (page) {
			case "account-overview":
				return <AccountDisplay />;
			case "create-account":
				return <CreateAccount handleClick={updateMainPageContent} />;
			// Add new cases here to add more navbar links
			default:
				return <AccountDisplay />;
		}
	};

	return (
		<ThemeProvider theme={themeMode}>
			<GlobalStyles />
			<div className="main-page-container container-view">
				<div className="header">
					<div className="header-welcome-box">
						Welcome, <br />
						<span className="header-username">
							{userData.firstName} {userData.lastName}
						</span>
					</div>
					<div className="main-upper-buttons">
						<button id="profile" onClick={toProfile}>
							{" "}
							Profile
						</button>
						<button id="logout-button" onClick={handleLogout}>
							{" "}
							Log Out
						</button>


						<Toggle
							id="main-theme-button"
							theme={theme}
							toggleTheme={themeToggler}
						/>
					</div>
				</div>



				<NavBar handleClick={updateMainPageContent} />

				<div className="main-page-content">
					{mainPageContentComponent()}
				</div>

				<Footer />
			</div>
		</ThemeProvider>
	);
}

export default MainPageView2;