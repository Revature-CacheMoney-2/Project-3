import axios from "axios";
import React, { useEffect, useState } from "react";
import config from "../../config.js";
import store from "../../store/Store.js";
import CurrencyFormat from "react-currency-format";


function PersonalInfo(props){

    const [personalInfo, setPersonalInfo] = useState([]);

    const handleProfileUpdate = () => {
        console.log("Information loaded", props.doProfileUpdate);
        // get all of a user's accounts
        axios
            .get(`${config.url}users/login`, {
                headers: {
                    username: store.getState().userReducer.username,
                    firstname: store.getState().userReducer.firstname,
                    lastname: store.getState().userReducer.lastname,
                },
            })
            .then((response) => {
                const allInfo = response.data;
                setAccounts(allInfo);
            })
            .catch((error) => console.error(`Error: ${error}`));
    };

    useEffect(() => {
        console.log("useEffect");
        handleProfileUpdate();
    }, [props.doTitleUpdate]);

    const content = personalInfo.map((info) => {
        return (
            <div
                className="info_item"
                key={info.accountId}
                id={account.accountId}
                onClick={handleAccountClick}
            >
                <div className="account_name">
                    <p>
                        {info.name} (***{account.accountId.toString().slice(-4)})
                    </p>
                </div>
                <div className="account_item_info">
                    <div className="account_type">
                        <p>{account.type}</p>
                    </div>
                    <div className="account_balance">
                        <CurrencyFormat
                            value={account.balance}
                            displayType={"text"}
                            thousandSeparator={true}
                            decimalScale={2}
                            fixedDecimalScale={true}
                            prefix={"$"}
                        />
                    </div>
                </div>
            </div>
        );
    });

}

export default PersonalInfo;