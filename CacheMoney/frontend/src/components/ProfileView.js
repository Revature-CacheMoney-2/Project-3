import React, { useState } from "react";
import Footer from "./Footer.js";
import userStore from "../store/Store.js";
import InfoTable from "./InfoTable";
import { useNavigate, Route, Routes} from "react-router-dom";
import { useDarkMode } from "./style/useDarkMode";
import { ThemeProvider } from "styled-components";
import { GlobalStyles } from "./style/GlobalStyles";
import Toggle from "./style/Toggle";
import { lightTheme, darkTheme } from "./style/Themes";
import SideNav from "./SideNav.js";
import TestVieew from './TestVieew';



function ProfileView(props){
    const navigate = useNavigate();
    const [theme, themeToggler, mountedComponent] = useDarkMode();
    const themeMode = theme === "light" ? lightTheme : darkTheme;

    const handleLogout = (event) => {
        userStore.dispatch({
            type: "LOGOUT_USER",
            payload: "",
        });

        navigate("/");
    };
    const toMain = (event) => {

        navigate("/main");
    };

    /*const updateProfilePageContent = (event) => {
        setPage(event.target.id);
        profilePageContentComponent(event.target.id);
    };
    const profilePageContentComponent = () => {
        switch (page) {
            case "sign-in":
                return <SignInDisplay />;
            case "address":
                return <AddressDisplay  />;
            case "phone":
                return <PhoneDisplay  />;
            case "email":
                return <EmailDisplay  />;
            // Add new cases here to add more navbar links
            default:
                return <DefaultDisplay />;
        }
    };*/


    return (
        <ThemeProvider theme={themeMode}>
            <GlobalStyles />
            <div className="profile-page-container container-view">
                <div className="header">
                    <button id="main-page-button" onClick={toMain}>
                        {" "}
                        Home
                    </button>
                    <div className="header-welcome-box">
                        Profile
                    </div>
                    <div className="main-upper-buttons">
                        <button id="logout-button" onClick={handleLogout}>
                            {" "}
                            Log Out
                        </button>
                    </div>
                </div>
                <SideNav />
                <Routes>
                    <Route path='test' element={<TestVieew />} />
                </Routes>


                <div className="footer-container">
                    <Footer />
                </div>
            </div>
        </ThemeProvider>
    );
}

export default ProfileView;

