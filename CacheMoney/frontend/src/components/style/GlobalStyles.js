import { createGlobalStyle } from "styled-components";
export const GlobalStyles = createGlobalStyle`
body {
	color: ${({ theme }) => theme.text};
}

a {
	color: ${({ theme }) => theme.highlight};
}

/* scrollbar styling */
::-webkit-scrollbar {
  width: 15px;
}

::-webkit-scrollbar-track {
  border-radius: 10px;
}
 
::-webkit-scrollbar-thumb {
  background: ${({ theme }) => theme.backlight}; 
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background: ${({ theme }) => theme.ultrahighlight}; 
}

/* end scrollbar styling */

/* splash page styling */

#splash-inner-container {
	background-color: ${({ theme }) => theme.bread};
}

#splash-mode-button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	border: 3px solid ${({ theme }) => theme.backlight};
}

#splash-mode-button:hover {
	background-color: ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.text};
}

#register-here {
	color: ${({ theme }) => theme.backlight};
}

#register-here:hover {
	color: ${({ theme }) => theme.ultrahighlight};
}

#splash-button {
	background-color: ${({ theme }) => theme.text};
	box-shadow: -5px 5px 0px 1px ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.shadow};
}

#splash-button:hover {
	background-color: ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.text};
}

#splash-mode-button {
	border: 5px solid ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.backlight};
}

#splash-mode-button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.backlight};
}

.benjamin button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	border: 5px solid ${({ theme }) => theme.backlight};
}

.benjamin button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.backlight};
}

/* splash page styling end */

/*chatbot styling */

.chatbot-box {
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.shadow};
}

.react-chatbot-kit-chat-container {
	background-color: ${({ theme }) => theme.bread};
}

.react-chatbot-kit-chat-bot-message-container {
	color: ${({ theme }) => theme.text};
	text-align: center;
	font-weight: 700;
}

.react-chatbot-kit-chat-header {
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.shadow};
}

.option-button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
}

.option-button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.backlight};
  }

  .react-chatbot-kit-chat-input {
	color: ${({ theme }) => theme.bread};
	background-color: ${({ theme }) => theme.text};
  }
  
  .react-chatbot-kit-chat-input:focus {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.shadow};
  }
  
  .react-chatbot-kit-chat-input::placeholder {
	color: ${({ theme }) => theme.bread};
  }
  
  .react-chatbot-kit-chat-input::placeholder:focus {
	color: ${({ theme }) => theme.text};
  }

  .react-chatbot-kit-chat-btn-send {
	background-color: ${({ theme }) => theme.highlight};
  }

  .react-chatbot-kit-chat-btn-send:hover {
	background-color: ${({ theme }) => theme.ultrahighlight};
  }

  .read-more-link {
	color: ${({ theme }) => theme.bread};
	background-color: ${({ theme }) => theme.text};
  }

  .read-more-link:hover {
	color: ${({ theme }) => theme.highlight};
  }

  #popup-2 {
	background: ${({ theme }) => theme.shadow};
  }

  #popup-2 a {
	  color: ${({ theme }) => theme.backlight};
	  font-weight: 900;
  }

  #popup-2 a:hover {
	  color: ${({ theme }) => theme.text}
  }

  [role=tooltip].popup-content {
	  box-shadow: 0 0 10px rgb(0 0 0 / 50%) !important;
  }

/* chatbot styling end */

/* login styling */

.password-error {
	border: 1px solid ${({ theme }) => theme.error};
}

.password-ok {
	border: 1px solid ${({ theme }) => theme.greyness};
}

.login-outer-container {
	background-image: ${({ theme }) => theme.moneyBackground};
	background-position: center;
	background-size: cover;
	background-repeat: no-repeat;
}

.login-inner-container {
	background-color: ${({ theme }) => theme.bread};
}

.login-white-box {
	color: ${({ theme }) => theme.bread};
	background-color: ${({ theme }) => theme.text};
}

.login {
	background-color: ${({ theme }) => theme.highlight};
	color: ${({ theme }) => theme.text};
}

.login:hover {
	text-shadow: 0px 0px 5px ${({ theme }) => theme.greyness};
	background-image: ${({ theme }) => theme.moneyBackground};
	background-size: 75%;
}

#login-theme-button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	border: 3px solid ${({ theme }) => theme.backlight};
}

#login-theme-button:hover {
	background-color: ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.text};
}

.login-outer-container input[type="text"], .login-outer-container input[type="password"] {
	border: 2px solid ${({ theme }) => theme.bread};
	box-shadow: -4px 4px 0px ${({ theme }) => theme.gentle};
}

#login-error-box {
	color: ${({ theme }) => theme.error};
}

/* login styling end */

/* registration styling */

#registration-error {
    color: ${({ theme }) => theme.error};
}

::selection {
	background: ${({ theme }) => theme.highlight};
	color: ${({ theme }) => theme.text};
}

input[type="text"]:focus, input[type="password"]:focus {
	background-color: ${({ theme }) => theme.gentle};
	color: ${({ theme }) => theme.text};
}

input[type="text"], input[type="password"] {
	color: ${({ theme }) => theme.greyness};
}

.err-desc {
	color: ${({ theme }) => theme.error};
}

#splash-outer-container {
	background-image: ${({ theme }) => theme.splashBackground};
	background-position: center;
	background-size: cover;
	background-repeat: no-repeat;
}

#register-theme-button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	border: 3px solid ${({ theme }) => theme.backlight};
}

#register-theme-button:hover {
	background-color: ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.text};
}

/* registration styling end */

/* mainpage styling */

.logo-bigger {
	text-shadow: -2px 4px 0px ${({ theme }) => theme.backlight};
}

.main-container-content {
	background-color: ${({ theme }) => theme.bread};
}

.main-page-container {
	background-color: ${({ theme }) => theme.background}
}

.header {
	background-color: ${({ theme }) => theme.bread};
}

.main-content-inner {
	background-color: ${({ theme }) => theme.background};
}

.main-page-content {
	background-color: ${({ theme }) => theme.background};
}

.header-username {
	text-shadow: -1px 2px 0px ${({ theme }) => theme.highlight};
}

/* end mainpage styling */

/* mainpage account styling */

.account-type-container {
	color: ${({ theme }) => theme.greyness};
}

.account-name {
	background-color: ${({ theme }) => theme.bread};
}

.transfer-container {
	color: ${({ theme }) => theme.greyness};
}

.no-account-message {
	color: ${({ theme }) => theme.backlight};
}

.deposit-form button, .withdraw-form button, .transfer-form button {
	color: ${({ theme }) => theme.shadow};
	background-color: ${({ theme }) => theme.text};
	box-shadow: -4px 4px 0px ${({ theme }) => theme.gentle};
}

.deposit-form button:hover, .withdraw-form button:hover, .transfer-form button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.shadow};
	border: 2px solid ${({ theme }) => theme.gentle};
}

#deposit-input, #withdraw-input, #transfer-input, #transfer-description  {
	color: ${({ theme }) => theme.bread};
	background-color: ${({ theme }) => theme.text};
  }
  
#deposit-input:focus #withdraw-input:focus, #transfer-input:focus, #transfer-description:focus {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.shadow};
}
  

.account_create_form {
    color: ${({ theme }) => theme.greyness};
    border: 1px solid ${({ theme }) => theme.greyness};
	background-color: 
}

.account_item {
	border: 2px solid ${({ theme }) => theme.shadow};
}

.account_name {
	background-color: ${({ theme }) => theme.shadow};
}

.account_type {
	border: 1px solid ${({ theme }) => theme.shadow};
	border-bottom-left-radius: 8px;
	background-color: ${({ theme }) => theme.gentle};
}

.account_balance {
	border-top: 1px solid ${({ theme }) => theme.shadow};
	background-color: ${({ theme }) => theme.gentle};
}

.account_create_form {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.gentle};
	border: 3px solid ${({ theme }) => theme.shadow};
	box-shadow: -8px 8px  ${({ theme }) => theme.shadow};
}

.account_create_name input {
	color: ${({ theme }) => theme.shadow};
	background-color: ${({ theme }) => theme.text};
	border: none;
}

.account_create_radio_button input{
	accent-color: ${({ theme }) => theme.backlight};
	color: ${({ theme }) => theme.text};
}

.account_create_submit_button {
	color: ${({ theme }) => theme.backlight};
	background-color: ${({ theme }) => theme.text};
	box-shadow: -5px 5px ${({ theme }) => theme.shadow};
}

.account_create_submit_button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.backlight};
}

.account-options-container {
	background-color: ${({ theme }) => theme.shadow};
}

.account-option {
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.shadow};
	box-shadow: -3px 3px ${({ theme }) => theme.gentle};
}

.account-option:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.shadow};
	border: 2px solid ${({ theme }) => theme.gentle};
}

.account-options-container thead {
	background-color: ${({ theme }) => theme.gentle};
	color: ${({ theme }) => theme.shadow};
}

#selectFilter, #selectAccount {
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.shadow};
}

#selectFilter option {
	color: ${({ theme }) => theme.shadow};
	font-weight: 700;
}

#transaction-list {
	background-color: ${({ theme }) => theme.text};
	color: ${({ theme }) => theme.shadow};
}

tr:nth-child(even) {
	background-color: ${({ theme }) => theme.shadow};
	color: ${({ theme }) => theme.text};
}

.positive_balance {
	color: ${({ theme }) => theme.bread};
}

.negative_balance {
	color: ${({ theme }) => theme.error};
}

/* mainpage account styling end */

/* navigation styling */

.navigation-bar {
	background-color: ${({ theme }) => theme.bread};
	border-bottom: 10px solid ${({ theme }) => theme.shadow};
}

.navigation-bar a {
	color: ${({ theme }) => theme.text};
}

#logout-button {
	color: ${({ theme }) => theme.greyness};
	background-color: ${({ theme }) => theme.text};
	box-shadow: -4px 4px 0px ${({ theme }) => theme.shadow};
}

#logout-button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.highlight};}

#main-theme-button {
	color: ${({ theme }) => theme.greyness};
	background-color: ${({ theme }) => theme.text};
	box-shadow: -4px 4px 0px ${({ theme }) => theme.shadow};
}

#main-theme-button:hover {
	color: ${({ theme }) => theme.text};
	background-color: ${({ theme }) => theme.highlight};}
}

/* navigation styling end */

/* request styling */

.RequestFormContainer {
	color: ${({ theme }) => theme.text};
	border-top: 1px solid ${({ theme }) => theme.shadow};
    background-color: ${({ theme }) => theme.gentle};
    border: 3px solid ${({ theme }) => theme.shadow};
    box-shadow: -8px 8px  ${({ theme }) => theme.shadow};border: 1px solid ${({ theme }) => theme.shadow};
    border-bottom-left-radius: 8px;
    background-color: ${({ theme }) => theme.gentle};
    margin-left: 50px;
    display: flex;
    flex-direction: column;
    box-sizing: content-box;
    width: 50%;
    height: 110%;
    border-top-left-radius: 4px;
    align-items: center;
    padding-top: .4%;
    padding-bottom: 3%;
    font-size: 15px;
}

.RequestFormHeader {
    background-color: ${({ theme }) => theme.shadow};
    height:20px;
    width:200%;
    padding-left: 20%;
    padding-left: 5px;
    border-top-left-radius: 8px;
    -webkit-background-size: contain;
    -moz-background-size: contain;
    -o-background-size: contain;
    background-size: contain;
    padding-top: 4%;
    padding-bottom: 9%;
    background-repeat: no-repeat;
    text-align: center;
    font-weight: bold;
}

/* request styling end */

/* request list styling */
.RequestListHeaderContainer {
background-color: ${({ theme }) => theme.shadow};
border-top-right-radius: 25px;
width: 30%;
text-align: center;

}
.Request {
    color: ${({ theme }) => theme.text};
    border-top: 1px solid ${({ theme }) => theme.shadow};
    border-bottom: 2px solid;
    border-color: black;
    background-color: ${({ theme }) => theme.gentle};
    /*border: 3px solid ${({ theme }) => theme.shadow};*/
    /*box-shadow: -8px 8px  ${({ theme }) => theme.shadow};border: 1px solid ${({ theme }) => theme.shadow};*/
    background-color: ${({ theme }) => theme.gentle};
    display: flex;
    flex-direction: column;
    box-sizing: content-box;
    font-size: 15px;
    padding-left: 2%;
    padding-right: 2%;
    margin-bottom: .2%;
}


/* Request list styling end */

/* footer styling */

.footer {
	background-color: ${({ theme }) => theme.bread};
}

#footer-logo {
	width: 250px;
	height: 50px;
	background-image: ${({ theme }) => theme.cacheMoney};
	background-position: center;
	background-size: contain;
	background-repeat: no-repeat;
}

/* footer styling */

/* 404 Page Not Found styling */

.not-found-inner-container {
    background-color: ${({ theme }) => theme.bread};
}

.not-found-home-button {
    background-color: ${({ theme }) => theme.text};
    color: ${({ theme }) => theme.highlight};
}

.not-found-home-button:hover {
    box-shadow: -5px 5px ${({ theme }) => theme.bread};
    color: ${({ theme }) => theme.gentle};
}

.not-found-home-text {
    text-shadow: -2px 2px ${({ theme }) => theme.shadow};
}

/* 404 Page Not Found styling end */

/* media reactivity styling */

@media only screen and (max-width: 1080px) {
	#splash button,
	#splash-button input[type="submit"] {
		background-color: ${({ theme }) => theme.text};
		box-shadow: -5px 5px 0px 1px ${({ theme }) => theme.highlight};
		color: ${({ theme }) => theme.shadow}; 
	}
}

/* media reactivity styling end */

/* GLOBAL STYLING END */
`;
