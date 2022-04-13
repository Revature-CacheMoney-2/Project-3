import React, { useState } from "react";
import userStore from "../store/Store.js";

function EmailDisplay(){
    //const [account, setAccount] = useState({});
    let userData = userStore.getState().userReducer;
    return (
        <div className="email-outer-container">
            <div className="content-container">
                <p>Email</p>
                <hr></hr>
                <span className="email">
							{userData.email}
			</span>
            </div>
        </div>
    )
}

export default EmailDisplay;