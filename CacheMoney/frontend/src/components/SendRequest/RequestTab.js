/**
 * @author Shawntaria Burden, Sebastian Fierros, Ethan Edmond, Tyler Daniel
 */

import React, { useState } from "react";
import Request from "./Request";
import RequestList from "./RequestList";
import "./RequestTab.css";

const useRerenderer = () => {
    const [rerenderer, setRerenderer] = useState(false);
    return [rerenderer, () => {
        setRerenderer(!rerenderer);
    }];
}

const RequestTab = (props) => {
    const [rerenderer, rerender] = useRerenderer();

    return (
        <div className="RequestTab">
            <Request rerender={rerender}/>
            <RequestList rerender={rerender} rerenderer={rerenderer}/>
        </div>
    );
}

export default RequestTab;