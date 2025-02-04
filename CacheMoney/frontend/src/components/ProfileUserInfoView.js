import React, {useState} from 'react';
import userStore from "../store/Store.js";
//import store from "../store/Store.js";
import config from "../config";
import axios from "axios";

export default function ProfileUserInfoView() {
    let userData = userStore.getState().userReducer;

    let firstName = userData.firstName;
    let lastName = userData.lastName;
    let email = userData.email;
    console.log(userData);
    const [userInfo, updateUserInfo] = useState({
        firstName: firstName,
        lastName: lastName,
        email: email
    })

    function userInfoChanged(event) {
        updateUserInfo((prevState) => ({...prevState, [event.target.name]: event.target.value}));
    }

    function submit(event) {
        event.preventDefault();
        console.log("Test");
        if(validateInput()) {

            const body = JSON.stringify(userInfo);
            const headers = {'Content-Type': 'application/json', 'token': userData.token,
                'userId': userData.userId}
            const url = config.url;
            console.log(body);
            axios.patch(`${url}users/`, body, {headers: headers}).then((response) => {
                console.log(response);
                userStore.dispatch({
                    type: "UPDATE_NAME_FIRST",
                    payload: userInfo.firstName,
                });
                userStore.dispatch({
                    type: "UPDATE_NAME_LAST",
                    payload: userInfo.lastName,
                });
                userStore.dispatch({
                    type: "UPDATE_EMAIL",
                    payload: userInfo.email,
                });
            })
        }
    }

    function validateInput() {
        const namePattern = /^[a-zA-Z][a-zA-Z -]+[a-zA-Z]$/;
        if (!namePattern.test(userInfo.firstName)) {
            return false;
            // registrationError(
            //     "firstname",
            //     "firstname-span",
            //     "firstname-description",
            //     "Invalid first name",
            //     "Please check the spelling and try again."
            // );
        }
        if (!namePattern.test(userInfo.lastName)) {
            return false;
            // registrationError(
            //     "lastname",
            //     "lastname-span",
            //     "lastname-description",
            //     "Invalid last name",
            //     "Please check the spelling and try again."
            // );
        }

        const emailPattern =
            /^[a-zA-Z0-9._-]+@{1}[a-zA-Z0-9-_]+[.]{1}[a-zA-Z0-9]+[a-zA-Z_.-]*$/;
        if (!emailPattern.test(userInfo.email)) {
            console.log("email problem");
            return false;
            // registrationError(
            //     "email",
            //     "email-span",
            //     "email-description",
            //     "Invalid email address",
            //     "Please check your input and try again."
            // );
        }
        return true;
    }

    return (
        <div className="create-account-outer-container">
            <div className="create-account-inner-container">
                <div className="account_create_form" style={{height: '420px'}}>


                            <p className="account_create_form_header">Update Info</p>
                   <form>
                        <div className="account_create_name">
                            <label >First name:</label>
                            <input type='text' name='firstName' onChange={userInfoChanged} value={userInfo.firstName}  />
                        </div>
                        <div className="account_create_name">
                            <label >Last name:</label >
                            <input type='text' name='lastName' onChange={userInfoChanged} value={userInfo.lastName} />
                        </div>


                    <div className="account_create_name">
                        <label >Email:</label>
                        <input type='text' name='email' onChange={userInfoChanged} value={userInfo.email} />
                    </div>

                        <button  className="account_create_submit_button" onClick={submit} >Update</button>
                   </form>
                   </div>

            </div>
        </div>
    );
}