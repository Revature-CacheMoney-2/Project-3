import React, { useEffect } from "react";
import config from "../config";
import store from "../store/Store";
import Notification from "./Notification";
import {useState} from "react";
import axios from "axios";

function NavBar(props) {
	const [unreadNotifications, setUnreadNotifications] = useState([]);
	const [dotDisplay, setDotDisplay] = useState('none');
	useEffect(() => {
		getUnread();
	},[])

	useEffect(() => {
		const timer = setTimeout(() => {
			console.log(unreadNotifications);
			if (unreadNotifications.length > 0){
				setDotDisplay('block');
			} else {
				setDotDisplay('none');
			}
		  }, 1000);
		  return () => clearTimeout(timer);
	},[unreadNotifications])

	async function getUnread(){
		var unread;
		var user;
		await axios
				.get(`${config.url}users/`, {
					headers: {
						token: store.getState().userReducer.token,
						userId: store.getState().userReducer.userId,
					},
				})
				.then((response) => {
					user = response.data;
					console.log(user);
				})
				.catch((error) => console.error(`Error: ${error}`));
	
				await axios
				.get(`${config.url}notifications/unread/` + user.userId)
				.then((response) => {
					unread = response.data;
					setUnreadNotifications(unread);
					console.log(unread);
				})
				.catch((error) => console.error(`Error: ${error}`));
				return unread;
	}

	// Make sure to add additional cases to MainPageView to handle any routing changes/new links
	const [notificationDisplay, setNotificationDisplay] = useState('none');
	// @author Max Hilken, Mika Nelson, Cullen Kuch
	async function ToggleNotificationDisplay(){
		var user;
		getUnread();
		if (notificationDisplay == 'none'){
			setNotificationDisplay('block')
		} else {
			setNotificationDisplay('none')
		}
			await axios
			.get(`${config.url}users/`, {
				headers: {
					token: store.getState().userReducer.token,
					userId: store.getState().userReducer.userId,
				},
			})
			.then((response) => {
				user = response.data;
				console.log(user);
			})
			.catch((error) => console.error(`Error: ${error}`));

			await axios
			.put(`${config.url}notifications/update`, user)
			.catch((error) => console.error(`Error: ${error}`));
	}

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
					Create Account
				</span>
			</a>
			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="investment-portfolio"
				>
					Investment Portfolio
				</span>
			</a>
			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="send-request">
					Transfer Money
				</span>
			</a>
			<a href="#">
				<span
					className="navigation-link"
					onClick={props.handleClick}
					id="request-tab">
					Request Money
				</span>
			</a>
			<div className="Notification">
				<a href="#">
					<span
						className="navigation-link"
						onClick={() => ToggleNotificationDisplay()}
						id="Notification"
					>
						Notifications

					<span className = "notifDot" style={{display: dotDisplay}}/>
					</span>
					<span style={{display: notificationDisplay}}>
						<Notification unreadNotifications={unreadNotifications}/>
					</span>
				</a>
			</div>
		</div>
	);
}

export default NavBar;
