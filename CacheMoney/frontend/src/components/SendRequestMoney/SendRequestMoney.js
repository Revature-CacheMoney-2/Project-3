//import axios from "axios";
//import { useState } from "react";
//import config from "../../config";
//import store from "../../store/Store.js";
//
//function SendRequestMoney(props) {
//    // local formData state
//    const [formData, setFormData] = useState({
//        name: "",
//        balance: 0,
//        type: "",
//        user: {
//            userId: store.getState().userReducer.userId,
//        },
//    });
//
//    // retrieve the url from the config
//    const url = config.url;
//
//    // post transfer
//    const postTransfer = (transfer) => {
//        axios
//            .post(`${url}transfers`, transfer, {
//                headers: {
//                    token: store.getState().userReducer.token,
//                    userId: store.getState().userReducer.userId,
//                },
//            })
//            .catch((error) => console.error(`Error: ${error}`));
//    };
//
//    // updates form data when form changes
//    const handleChange = (event) => {
//        event.preventDefault();
//        setFormData({ ...formData, [event.target.name]: event.target.value });
//    };
//
//    // what the submit button should do
//    const handleSubmit = (event) => {
//        postTransfer(formData);
//        props.handleClick(event);
//        //navigate("/accounts");
//        alert(
//            "Transfer successfully completed! Please view your updated balance."
//        );
//    };
//
//    return (
//        <div className="send-money-outer-container">
//            <div className="send-money-inner-container">
//                <div className="send_money_form">
//                    <p className="send_money_form_header">Send/Request Money</p>
//
//                    <form>
//                        <div className="send_money_account_id">
//                            <label htmlFor="sender_account_id">Sender's Account ID</label>
//                            <input
//                                type="float"
//                                id="account_id"
//                                name="id"
//                                onChange={handleChange}
//                            />
//
//                            <label htmlFor="recipient_account_id">Recipient's Account ID</label>
//                            <input
//                                type="float"
//                                id="account_id"
//                                name="id"
//                                onChange={handleChange}
//                            />
//                        </div>
//
//                        <div className="account_create_radio_button_group">
//                            <p className="account_create_type_header">Account Type</p>
//
//                            <div>
//                                <div className="account_create_radio_button">
//                                    <input
//                                        type="radio"
//                                        id="checking"
//                                        name="type"
//                                        value="checking"
//                                        onChange={handleChange}
//                                    />
//                                    <label htmlFor="checking">Checking</label>
//                                </div>
//
//                                <div className="account_create_radio_button">
//                                    <input
//                                        type="radio"
//                                        id="savings"
//                                        name="type"
//                                        value="savings"
//                                        onChange={handleChange}
//                                    />
//                                    <label htmlFor="savings">Savings</label>
//                                </div>
//                            </div>
//                        </div>
//                    </form>
//
//                    <button
//                        className="send_money_submit_button"
//                        type="button"
//                        name="submit"
//                        id="send-money"
//                        onClick={handleSubmit}
//                    >
//                        Complete Transfer
//                    </button>
//                </div>
//            </div>
//        </div>
//    );
//}
//
//export default SendRequestMoney;