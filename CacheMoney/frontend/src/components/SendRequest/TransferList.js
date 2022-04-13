/**
 * @author Shawntaria Burden, Sebastian Fierros, Ethan Edmond, Tyler Daniel
 */

 import axios from "axios";
 import React, { useState, useEffect } from "react";
 import config from "../../config";
 import store from "../../store/Store";
 import "./RequestList.css";
 
const TransferList = (props) => {
    const [transfers, setTransfers] = useState([]);
    const [rerenderer, setRerenderer] = useState(false);

    useEffect(() => {
        const gettingSource = axios.get(`${config.url}transfer/source`,
        {
            headers: {
                token: store.getState().userReducer.token,
                userId: store.getState().userReducer.userId
            }
        })
        //returning a mapped transfer of response with data coming "towards us" as opposed to an array with data
        .then(response => {
            response.data = response.data.map(transfer => {
                transfer.direction = "towards us";
                return transfer;
            })
            return response;
        })
        const gettingDest = axios.get(`${config.url}transfer/destination`, {
            headers: {
                token: store.getState().userReducer.token,
                userId: store.getState().userReducer.userId
            }
        })
        .then(response => {
            response.data = response.data.map(transfer => {
                transfer.direction = "away from us";
                return transfer;
            })
            return response;
        })

        Promise.all([gettingSource, gettingDest])
        .then( values => {
            const transferMap = {};
            for (const value of values) {
                for (const transfer of value.data) {
                    transferMap[transfer.transferId] = transfer;
                }
            }
            setTransfers(Object.values(transferMap).reverse());
        })

    }, [rerenderer]);

    return (
        <div className="transferList">
            <div className="transferListHeaderContainer">
                <h1 className="transferListHeader">
                Your transfers
                </h1>
            </div>
            
        {
        transfers.map((transfer, i) => {
            return(
                <div key={i} className="transfer">
                    
                    <p style={{color: "black"}}>sourceAccountId: {transfer.sourceAccount.accountId}</p>
                    <p style = {{color: " black"}}>destinationAccountId: {transfer.destinationAccount.accountId}</p>
                    <p style= {{color: "black"}}>description: {transfer.description}</p>
                    <p style={{color: "black"}}>Amount: ${transfer.amount}</p>
                </div>
            )    
        })
        }
        </div>
    );
}

export default TransferList;
