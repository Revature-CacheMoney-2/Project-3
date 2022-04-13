import React from "react";
import Transfer from "./Transfer";
import TransferList from "./TransferList";
import "./RequestTab.css";

const TransferTab = (props) => {
    return (
        <div className="TransferTab">
            <Transfer/>
            <TransferList/>
        </div>
    );
}

export default TransferTab;