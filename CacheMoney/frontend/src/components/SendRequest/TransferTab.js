import React, { useState } from "react";
import Transfer from "./Transfer";
import TransferList from "./TransferList";
import "./RequestTab.css";

// TODO rerenders aren't working anymore, figure out why and fix it
const useRerenderer = () => {
    const [rerenderer, setRerenderer] = useState(false);
    return [rerenderer, () => {
        setRerenderer(!rerenderer);
    }];
}

const TransferTab = (props) => {
    const [rerenderer, rerender] = useRerenderer();

    return (
        <div className="TransferTab">
            <Transfer rerender={rerender}/>
            <TransferList/>
        </div>
    );
}

export default TransferTab;