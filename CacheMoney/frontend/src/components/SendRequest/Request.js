/**
 * @author Shawntaria Burden, Sebastian Fierros, Ethan Edmond, Tyler Daniel
 */

import axios from "axios";
import React, { useState, useEffect } from "react";
import config from "../../config";
import store from "../../store/Store";
import "./Request.css";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer, toast } from "react-toastify";

const Request = ({rerender}) => {
    const [accounts, setAccounts] = useState([]);

    //cutting a slice of state to hold form data
    const  [formData, setFormData]  = useState({
        sourceAccountId: 0,
        destinationAccountId: "",
        description: "",
        amount: 0,
    });

    useEffect(() => {
        axios.get(`${config.url}users/accounts`, {
            headers: {
                token: store.getState().userReducer.token,
                userId: store.getState().userReducer.userId,
            }})
        .then((response) => {
            setAccounts(response.data);
            setFormData({...formData, destinationAccountId: response.data[0].accountId})
        })
    },[]);

    const options = accounts.map(account => {
        return (
            <option key={account.accountId} value={account.accountId}>{account.name} (id: {account.accountId})</option>
        );
    });

    //setting formData using setFormData to update dynamically
    const handleChange = (event) => {
        let value = event.target.value;
        if(event.target.name == "sourceAccountId" || event.target.name == "amount"){
            value = parseInt(value, 10);
        }
        setFormData({...formData, [event.target.name]: value});
    }

    //handles submit request
    const handleSubmit = (submitRequest) => {
        submitRequest.preventDefault();
        const sendingToPost = {
            amount: formData.amount,
            sourceAccount: {accountId: formData.sourceAccountId},
            destinationAccount: {accountId: formData.destinationAccountId},
            description: formData.description
        }
        axios
            .post(`${config.url}request`, sendingToPost, {
                headers: {
                    token: store.getState().userReducer.token,
                    userId: store.getState().userReducer.userId,
                }
            })
            .then((response) => {
                setFormData({
                    sourceAccountId: 0,
                    destinationAccountId: accounts[0],
                    description: "",
                    amount: 0,
                })
                //only notify upon successful 200
                response.status==200?
                toast.success('Request Processed Successfully', {
                    position: "bottom-right",
                    autoClose: 2000,
                    hideProgressBar: true,
                    closeOnClick: true,
                    pauseOnHover: true,
                    draggable: true,
                    progress: undefined,
                }):toast.error('error')
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
                })
    }
    return (
        <div className="RequestFormContainer">
        <ToastContainer/>
        <form className="RequestForm transfer-form" onSubmit={handleSubmit}>
            <h1 className="RequestFormHeader">
                Make a Request
            </h1>

            <div className="Input">
                <label htmlFor="source-id">Source Account</label>
                <input id="source-id" type="number" min={0} step={1} max={2147483647} name="sourceAccountId" onChange={handleChange}/>
            </div>
            
            <div className="Input">
                <label htmlFor="destination-id">Destination Account</label>
                <select value={formData.destinationAccountId} id="destination-id" name="destinationAccountId" onChange={handleChange}>
                    {accounts.length == 0 && <option value={null}>No Accounts to be Displayed</option>}
                    {options}
                </select>
            </div>

            <div className="Input">
                <label htmlFor="amount">Amount of Money</label>
                <input id="amount" type="number" min={0.01} step={0.01} name="amount" onChange={handleChange}/>
            </div>

            <div className="Input">
                <label htmlFor="description">Memo/Description</label>
                <input id="description" type="text" maxLength={255} name="description" onChange={handleChange}/>
            </div>
            
            <button id="submit-request"> Request Money </button>
        </form>
        </div>
    );
}

export default Request;
