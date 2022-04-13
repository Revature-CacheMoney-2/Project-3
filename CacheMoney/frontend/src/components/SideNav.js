import React from "react";
import {Link} from 'react-router-dom'
function SideNav(props) {

    return (
        <div id="mySidenav" className="sidenav">
			<span
                className="navigation-link"
                //onClick={}
                id="sign-in"
            >
				Sign-In
			</span>

            <span
                className="navigation-link"
                //onClick={}
                id="address"
            >
				Address
			</span>

            <span
                className="navigation-link"
                //onClick={}
                id="phone"
            >
				Phone
			</span>

            <Link to='/profile/test'
                className="navigation-link"
                //onClick={}
                id="email"
            >
				Email
			</Link>

        </div>
    );
}

export default SideNav;