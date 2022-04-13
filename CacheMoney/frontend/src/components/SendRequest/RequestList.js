/**
 * @author Shawntaria Burden, Sebastian Fierros, Ethan Edmond, Tyler Daniel
 */

import axios from "axios";
import React, { useState, useEffect } from "react";
import config from "../../config";
import store from "../../store/Store";
import "./RequestList.css";

const RequestList = ({rerender, rerenderer}) => {
    const [requests, setRequests] = useState([]);

    useEffect(() => {
        const gettingSource = axios.get(`${config.url}request/source`,
        {
            headers: {
                token: store.getState().userReducer.token,
                userId: store.getState().userReducer.userId
            }
        })
        //returning a mapped request of response with data coming "towards us" as opposed to an array with data
        .then(response => {
            response.data = response.data.map(request => {
                request.direction = "towards us";
                return request;
            })
            return response;
        })
        const gettingDest = axios.get(`${config.url}request/destination`, {
            headers: {
                token: store.getState().userReducer.token,
                userId: store.getState().userReducer.userId
            }
        })
        .then(response => {
            response.data = response.data.map(request => {
                request.direction = "away from us";
                return request;
            })
            return response;
        })

        Promise.all([gettingSource, gettingDest])
        .then( values => {
            setRequests(values.reduce((acc, curr) => {
                return [...acc, ...curr.data];
            }, []));
        })

    }, [rerenderer]);

    const onAccept = (requestId) => {
        return () => {
            axios.post(`${config.url}request/accept/${requestId}`, {}, {
                headers: {
                    token: store.getState().userReducer.token,
                    userId: store.getState().userReducer.userId
                }
            })
            .then(() => {
                // TODO put a toast.success here
                rerender();
            })
            .catch(() => {
                // TODO put a toast.error here
            })
        }
    }

    const onDecline = (requestId) => {
        return () => {
            axios.get(`${config.url}request/delete/${requestId}`, {
                headers: {
                    token: store.getState().userReducer.token,
                    userId: store.getState().userReducer.userId
                }
            })
            .then(() => {
                rerender();
                // TODO put a toast.success here
            })
            .catch(() => {
                // TODO put a toast.error here
            })
        }
    }

    return (
        <div className="RequestList">
            <div className="RequestListHeaderContainer">
                <h1 className="RequestListHeader">
                Your Requests
                </h1>
            </div>
            
        {
        requests.map((request, i) => {
            return(
                <div key={i} className="Request">
                    
                    <p style={{color: "black"}}>Source Account#: {request.sourceAccount.accountId}</p>
                    <p style = {{color: " black"}}>Destination Account#: {request.destinationAccount.accountId}</p>
                    <p style= {{color: "black"}}>Memo/Description: {request.description}</p>
                    <p style={{color: "black"}}>Amount: ${request.amount}</p>
                    {
                        (request.direction == "towards us")  ? 
                            (
                                <>
                                <button onClick={onAccept(request.requestId)}>Accept Request</button>
                                <button onClick={onDecline(request.requestId)}>Decline Request</button>
                                </>
                            ) : (
                                <button onClick={onDecline(request.requestId)}>Cancel Request</button>
                            )
                    }
                </div>
            )    
        })
        }
        </div>
    );
}

export default RequestList;