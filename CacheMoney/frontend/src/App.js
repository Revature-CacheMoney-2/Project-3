import "./css/App.css";
import React from "react";
import SplashView from "./components/SplashView.js";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SigninView from "./components/SigninView.js";
import RegisterView from "./components/RegisterView.js";
import NotFoundView from "./components/NotFoundView.js";
import MainPageView from "./components/MainPageView";
import TransactionFilter from "./components/Transaction/TransactionFilter";

function App() {
	return (
		<>
			<Router>
				<Routes>
					<Route path="/" exact element={<SplashView />} />
					<Route path="/signin" exact element={<SigninView />} />
					<Route path="/register" exact element={<RegisterView />} />
					<Route path="/main" exact element={<MainPageView />} />
					<Route path="/transactions" exact element={<TransactionFilter />} />
					<Route path="*" exact element={<NotFoundView />} />
				</Routes>
			</Router>
		</>
	);
}

export default App;
