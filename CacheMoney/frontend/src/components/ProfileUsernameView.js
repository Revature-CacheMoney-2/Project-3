import {useState} from 'react';
import axios from 'axios';
import config from "../config";

export default function ProfileUsernameView() {
    const [newUsername, updateNewUsername] = useState('');

    function newUsernameChanged(event) {
        updateNewUsername(event.target.value);
    }

    function submit() {
        if(validateUsername()) {
            const body = { username: newUsername};
            const headers = {}
            const url = config.url;
            axios.patch(`${url}users/`)
        }
    }

    function validateUsername() {
        const usernamePattern = /^[a-zA-Z0-9@~._-]{8,}$/;
        if (!usernamePattern.test(newUsername)) {
            registrationError(
                "username",
                "username-span",
                "username-description",
                "Invalid username",
                "Usernames should be between 8-255 characters in length and use alphanumeric / select symbols.."
            );
            return false;
        }
        return true;
    }

    return (
        <div>
            <div>
                Old username:
            </div>
            <div>
                New username:
                <input type='text' value={newUsername} onChange={newUsernameChanged} />
            </div>
            <div>
                <button >Update</button>
            </div>
        </div>
    )
}