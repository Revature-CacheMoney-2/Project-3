/**
 * @author Shawntaria Burden, Sebastian Fierros, Ethan Edmond, Tyler Daniel
 */
import React, { useState, useEffect } from "react";
import axios from "axios";
import config from "../../config";
import store from "../../store/Store";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer, toast } from "react-toastify";

function Transfer({rerender}) {
    const [accounts, setAccounts] = useState([]);
    const  [formData, setFormData]  = useState({
        sourceAccountId: "",
        destinationAccountId: 0,
        description: "",
        amount: 0,
    });

    useEffect(() => {
        // retrieve all user's accounts
        axios
            .get(`${config.url}users/accounts`, {
                headers: {
                    token: store.getState().userReducer.token,
                    userId: store.getState().userReducer.userId,
                },
            })
            .then((response) => {
                setAccounts(response.data);
                setFormData({...formData, sourceAccountId: response.data[0].accountId})
            }).catch((error) => {
                toast.error('Could not get Accounts', {
                    position: "bottom-right",
                    autoClose: 2000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                })
            });
    }, []);

    // post transfer transaction
    const postTransfer = (transaction) => {
        axios
            .post(`${config.url}transfer`, transaction, {
                headers: {
                    token: store.getState().userReducer.token,
                    userId: store.getState().userReducer.userId,
                },
            })
            .then((res) => {
                toast.success('Request Processed Successfully', {
                    position: "bottom-right",
                    autoClose: 2000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                })
                rerender();
            })
            .catch((error) => {
                toast.error('Request failed', {
                    position: "bottom-right",
                    autoClose: 2000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                })
            });
    };

    // what the submit button should do
    const handleSubmit = (event) => {
        // prevent page reloading
        event.preventDefault();

        // create the transfer payload
        const transfer = {
            amount: formData.amount,
            sourceAccount: {accountId: formData.sourceAccountId},
            destinationAccount: {accountId: formData.destinationAccountId},
            description: formData.description
        }

        // perform the post
        postTransfer(transfer);
    };

    const options = accounts.map(account => {
        return (
            <option key={account.accountId} value={account.accountId}>{account.name} (id: {account.accountId})</option>
        );
    });

    const handleChange = (event) => {
        let value = event.target.value;
        if(event.target.name === "destinationAccountId" || event.target.name === "amount" || event.target.name === "sourceAccountId"){
            value = parseInt(value, 10);
        }
        setFormData({...formData, [event.target.name]: value});
    }

    return (
                <div className="transfer-form-container">
                    <ToastContainer />
                    <form onSubmit={handleSubmit} className="transfer-form">
                    <h1 className="transfer-header">Money Transfer</h1>
                        <div className="transfer-from-account" id="input">
                            <label>From: </label>
                            <select name="sourceAccountId" onChange={handleChange} value={formData.sourceAccountId}>
                                {accounts.length === 0 && <option value={null}>No Accounts to be Displayed</option>}
                                { options }
                            </select>    
                        </div>

                        <div className="transfer-to-account" id="input">
                            <label>Recipient's Account ID: </label>
                            <input name="destinationAccountId" type="number" min="0" step="1" onChange={handleChange}/><br></br>
                        </div>

                        <div className="transfer-amount" id="input">
                            <label>Transfer Amount: </label>
                            <input
                                type="number"
                                min="0.00"
                                step="0.01"
                                id="transfer-input"
                                name="amount"
                                onChange={handleChange}
                            />
                        </div>

                        <div className="transfer-description" id="input">
                            <label>Memo/Description: </label>
                            <input type="text" id="transfer-description" name="description" onChange={handleChange}/>
                        </div>

                        <button id="submit-request" className="transfer-button"
                        >
                            Send Transfer
                        </button>
                    </form>
                </div>
    );
}

export default Transfer;
