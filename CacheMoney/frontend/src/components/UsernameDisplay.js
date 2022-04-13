import React, { useState } from "react";
import userStore from "../store/Store.js";

function UsernameDisplay(){
    //const [account, setAccount] = useState({});
    let userData = userStore.getState().userReducer;
    return (
        <div className="username-outer-container">
            <div className="content-container">
                <p>Username</p>
                <hr></hr>
                <span className="username">
							{userData.username}
			</span>
            </div>
        </div>
    )
}

export default UsernameDisplay;