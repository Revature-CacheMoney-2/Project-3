import React, { useState } from "react";
import Footer from "./Footer.js";
import userStore from "../store/Store.js";
import InfoTable from "./InfoTable";
import { useNavigate } from "react-router-dom";
import { useDarkMode } from "./style/useDarkMode";
import { ThemeProvider } from "styled-components";
import { GlobalStyles } from "./style/GlobalStyles";
import Toggle from "./style/Toggle";
import { lightTheme, darkTheme } from "./style/Themes";
import config from "../config";
import axios from "axios";
import store from "../store/Store.js";
import ReactDOM from "react-dom";


function registrationError(password1, password1Span, passwordsDoNotMatch) {
    
}

function ResetPasswordView(){
    // function detectChange logs all the password changes
    function detectChange(e){
        console.log(e.target.value)
    }
    function MyForm() {
        const [name, setName] = useState("");
    }
    const [oldPassword, updateOldPassword ] = useState('');
    const [newPassword, updateNewPassword ] = useState('');
    const [confirmPassword, updateConfirmPassword ] = useState('');
    const url = config.url;

    function oldPasswordChanged(event){
        updateOldPassword(event.target.value);
    }
    function newPasswordChanged(event){
        updateNewPassword(event.target.value);
    }
    function confirmPasswordChanged(event){
        updateConfirmPassword(event.target.value);
    }

    const navigate = useNavigate();
    const [theme, themeToggler, mountedComponent] = useDarkMode();
    const themeMode = theme === "light" ? lightTheme : darkTheme;

    function ResetPassword(){
        if (validation()){
            const body = JSON.stringify({password: newPassword});
            const headers = {'Content-Type': 'application/json', 'token': store.getState().userReducer.token,
                'userId': store.getState().userReducer.userId, "oldPassword": oldPassword}

            axios.patch(`${url}users`, body, {headers: headers}).then((response)=> {

                console.log(response);
            })
        }
    }

    function validation(){
        const passwordPattern = /^[a-zA-Z0-9@^%$#/\\,;|~._-]{8,50}$/;
        if (!passwordPattern.test(oldPassword)) {
            registrationError(
                "password1",
                "password1-span",
                "password1-description",
                "Invalid password",
                "Passwords should be between 8-50 characters in length and use alphanumeric / select symbols.."
            );
            registrationError(
                "password2",
                "password2-span",
                "password2-description",
                "Invalid password",
                "Passwords should be between 8-50 characters in length and use alphanumeric / select symbols.."
            );
        }
        if (newPassword !== confirmPassword) {
            registrationError(
                "password1",
                "password1-span",
                "Passwords do not match.")
        return false;
        }
    return true;
    }
    return (
        <div className="create-account-outer-container">
            <div className="create-account-inner-container">
                <div className="account_create_form">
                    <p className="account_create_form_header">Reset Password</p>

                    <form>
                        <div className="account_create_name">
                            <label htmlFor="account_name">old password</label>
                            <input
                                type="text"
                                id="account_name"
                                name="name"
                                onChange={oldPasswordChanged}
                            />
                        </div>

                        <div className="account_create_name">
                            <label htmlFor="account_name">new password</label>
                            <input
                                type="text"
                                id="account_name"
                                name="name"
                                onChange={newPasswordChanged}
                            />
                        </div>

                        <div className="account_create_name">
                            <label htmlFor="account_name">confirm password</label>
                            <input
                                type="text"
                                id="account_name"
                                name="name"
                                onChange={confirmPasswordChanged}
                            />

                        </div>
                    </form>

                    <button
                        className="account_create_submit_button"
                        type="button"
                        name="submit"
                        id="create-new-account"
                        onClick={ResetPassword}
                    >
                        Submit
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ResetPasswordView;
