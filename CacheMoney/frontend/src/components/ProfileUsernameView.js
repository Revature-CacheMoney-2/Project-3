import {useState} from 'react';
import axios from 'axios';
import config from "../config";
import userStore from "../store/Store.js";
import store from "../store/Store.js";

export default function ProfileUsernameView() {
    const [newUsername, updateNewUsername] = useState('');

    let userData = userStore.getState().userReducer;

    function newUsernameChanged(event) {
        updateNewUsername(event.target.value);
    }

    function submit(event) {
        event.preventDefault();
        console.log("In method");
        if(validateUsername()) {
            const body = { username: newUsername};
            const headers = {'Content-Type': 'application/json', 'token': store.getState().userReducer.token,
                'userId': userData.userId}
            const url = config.url;
            axios.patch(`${url}users/`, body, {headers: headers}).then((response) => {
                console.log(response);
                userStore.dispatch({
                    type: "UPDATE_USERNAME",
                    payload: newUsername,
                });
            })
        }
    }

    function validateUsername() {
        const usernamePattern = /^[a-zA-Z0-9@~._-]{8,}$/;
        if (!usernamePattern.test(newUsername)) {
            // registrationError(
            //     "username",
            //     "username-span",
            //     "username-description",
            //     "Invalid username",
            //     "Usernames should be between 8-255 characters in length and use alphanumeric / select symbols.."
            // );
            return false;
        }
        return true;
    }

    return (
        <div>
            <div>
                Old username: {userData.username}
            </div>
            <div>
                New username:
                <input type='text' value={newUsername} onChange={newUsernameChanged} />
            </div>
            <div>
                <button onClick={submit}>Update</button>
            </div>
        </div>
    )
}