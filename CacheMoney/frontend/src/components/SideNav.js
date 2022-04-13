import React from "react";
import { Link } from "react-router-dom";
function SideNav(props) {

    return (
        <div id="mySidenav" className="sidenav">
            <button className="sidenav-button">
                <Link to="/profile/username"
                      className="page-link"

                      id="username"
                >

                    Username
                </Link>
            </button>
            <button className="sidenav-button">
                <Link to="/profile/password"
                      className="page-link"

                      id="password"
                >

                    Password
                </Link>
            </button>

            <button className="sidenav-button">
                <Link to="/profile/address"
                      className="page-link"

                      id="address"

                >
                    Address
                </Link>
            </button>

            <button className="sidenav-button">
                <Link to="/profile/phone"
                      className="page-link"

                      id="phone"

                >
                    Phone
                </Link>
            </button>

            <button className="sidenav-button">
                <Link to="/profile/email"
                      className="page-link"

                      id="email"

                >
                    Email
                </Link>
            </button>

        </div>
    );
}

export default SideNav;