import axios from "axios";
import React, { useState } from "react";
import { useDarkMode } from "./style/useDarkMode";
import { ThemeProvider } from "styled-components";
import { GlobalStyles } from "../components/style/GlobalStyles";
import Toggle from "./style/Toggle";
import { lightTheme, darkTheme } from "../components/style/Themes";
import { useNavigate } from "react-router-dom";
import config from "../config.js";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import QRModal from "./QRModal";

// The registrration component handles the registration form for new users.
// The info is persisted in the database and locally (partial).

function RegisterView() {
	const navigate = useNavigate();
	const [formData, setFormData] = useState({
		firstName: "",
		lastName: "",
		email: "",
		username: "",
		password1: "",
		password2: "",
		mfa: false,
	});

  const [qrCode, setQrCode] = useState("");
  const [qrFlag, setQrFlag] = useState(false);

  const handleChange = (event) => {
    event.preventDefault();
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  // handleSubmit: What happens when user presses the "submit" button on reg. form
  const handleSubmit = (event) => {
    event.preventDefault();
    if(validateInput()) {
      //console.log("Information being sent:  ", formData);
      doRegistration();
    }
  };

  //   // Checks input against validation (should be same patterns as used in backend)
  function validateInput() {
    const { firstName, lastName, email, username, password1, password2, mfa } =
      formData;
    let regValidity = true;
    if (password1 !== password2) {
      regValidity = false;
      registrationError(
        "password1",
        "password1-span",
        "password1-description",
        "Passwords do not match",
        "Please check your input and try again."
      );
      registrationError(
        "password2",
        "password2-span",
        "password2-description",
        "Passwords do not match",
        "Please check your input and try again."
      );
    }
    // Test the firstname, lastname for validity - ex. no empty strings
    const namePattern = /^[a-zA-Z][a-zA-Z -]+[a-zA-Z]$/;
    if (!namePattern.test(firstName)) {
      regValidity = false;
      registrationError(
        "firstname",
        "firstname-span",
        "firstname-description",
        "Invalid first name",
        "Please check the spelling and try again."
      );
    }
    if (!namePattern.test(lastName)) {
      regValidity = false;
      registrationError(
        "lastname",
        "lastname-span",
        "lastname-description",
        "Invalid last name",
        "Please check the spelling and try again."
      );
    }
    const emailPattern =
      /^[a-zA-Z0-9._-]+@{1}[a-zA-Z0-9-_]+[.]{1}[a-zA-Z0-9]+[a-zA-Z_.-]*$/;
    if (!emailPattern.test(email)) {
      regValidity = false;
      registrationError(
        "email",
        "email-span",
        "email-description",
        "Invalid email address",
        "Please check your input and try again."
      );
    }

    const usernamePattern = /^[a-zA-Z0-9@~._-]{8,}$/;
    if (!usernamePattern.test(username)) {
      regValidity = false;
      registrationError(
        "username",
        "username-span",
        "username-description",
        "Invalid username",
        "Usernames should be between 8-255 characters in length and use alphanumeric / select symbols.."
      );
    }

    const passwordPattern = /^[a-zA-Z0-9@^%$#/\\,;|~._-]{8,50}$/;
    if (!passwordPattern.test(password1)) {
      regValidity = false;
      registrationError(
        "password1",
        "password1-span",
        "password1-description",
        "Invalid password",
        "Passwords should be between 8-50 characters in length and use alphanumeric / select symbols.."
      );
      registrationError(
        "password2",
        "password2-span",
        "password2-description",
        "Invalid password",
        "Passwords should be between 8-50 characters in length and use alphanumeric / select symbols.."
      );
    }

    if (!regValidity) {
      document.getElementById("registration-error").innerHTML =
        "Error: Please correct invalid input.";
      return false;
    }

    return true;
  }

  function registrationError(
    id,
    spanId,
    descriptionId,
    errorTitle,
    errorDescription
  ) {
    let ele = document.getElementById(id);
    ele.style.border = "2px solid red";
    ele.style.boxShadow = "-4px 4px 0px #b04050";
    let errorSpan = document.getElementById(spanId);
    errorSpan.innerHTML = errorTitle;
    errorSpan.style.color = "#cc3040";
    errorSpan.style.margin = "0px 0px 0px 12px";
    let errorDescriptionSpan = document.getElementById(descriptionId);
    errorDescriptionSpan.innerHTML = errorDescription;
    errorSpan.style.color = "#cc3040";
    errorSpan.style.margin = "0px 0px 0px 12px";
  }

  // doRegistration: does the actual registration call
  function doRegistration() {
    let responseStatus;
    // Lets cover any sort of backend response
    let responseData;
    const url = config.url;

    const newUser = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      username: formData.username,
      password: formData.password1,
      mfa: formData.mfa,
    };
    axios
      .post(`${url}users/`, newUser)
      .then((response) => {
        //console.log(response);
        responseStatus = response.status;
        responseData = response.data;
        if (responseStatus === 200) {
          if (responseData.mfa === true && responseData.secretImageUri) {
            //console.log("Registration successful");
            //navigate("/qrcode");
            console.log(responseData);

            setQrCode(responseData.secretImageUri);

            setQrFlag(true);
          } else navigate("/signin");
        } else {
          // alert(
          // 	"Some Error occurred during registration. \n Check if the email or username is already in use?"
          // );
        }
      })
      .catch((error) => {
        toast.error("There was an issue creating an account", {
          position: "bottom-center",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
        console.error(`Error: ${error}`);
      });
  }

  const [theme, themeToggler, mountedComponent] = useDarkMode();
  const themeMode = theme === "light" ? lightTheme : darkTheme;

  if (!mountedComponent) return <div />;

  return (
    <ThemeProvider theme={themeMode}>
      <>
        <GlobalStyles />
        <ToastContainer
          position="bottom-center"
          autoClose={2000}
          hideProgressBar
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
        />
        <div className="container-view login-outer-container">
          <div className="login-inner-container">
            <div className="login-content-box">
              <Toggle
                id="register-theme-button"
                theme={theme}
                toggleTheme={themeToggler}
              />
              <h2 className="logo-smaller" id="register-logo">
                CacheMoney
              </h2>
              <div id="register-white-box" className="login-white-box">
                <div className="login-white-box-column">
                  <div className="error-container">
                    <span id="registration-error"></span>
                  </div>
                  <div id="registration-name-boxes">
                    <div id="box-L" className="reg-name-box">
                      <label htmlFor="firstName" id="label-L">
                        First name:
                        <span
                          className="detail-text"
                          id="firstname-span"
                        ></span>
                        <span
                          className="err-desc"
                          id="firstname-description"
                        ></span>
                      </label>
                      <input
                        type="text"
                        name="firstName"
                        className="reg-input-box"
                        id="firstname"
                        onChange={handleChange}
                        required
                      />
                    </div>

                    <div id="box-R" className="reg-name-box">
                      <label htmlFor="lastName" id="label-R">
                        Last name:
                        <span className="detail-text" id="lastname-span"></span>
                        <span
                          className="err-desc"
                          id="lastname-description"
                        ></span>
                      </label>
                      <input
                        type="text"
                        name="lastName"
                        className="reg-input-box"
                        id="lastname"
                        onChange={handleChange}
                        required
                      />
                    </div>
                  </div>

                  <div className="reg-field-box">
                    <label htmlFor="email">
                      Email:
                      <span className="detail-text" id="email-span">
                        *must be unregistered valid email
                      </span>
                      <span className="err-desc" id="email-description"></span>
                    </label>
                    <input
                      type="text"
                      name="email"
                      className="reg-input-box"
                      id="email"
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="reg-field-box">
                    <label htmlFor="username">
                      Username:
                      <span className="detail-text" id="username-span">
                        *must be unique
                      </span>
                      <span
                        className="err-desc"
                        id="username-description"
                      ></span>
                    </label>
                    <input
                      type="text"
                      name="username"
                      className="reg-input-box"
                      id="username"
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="reg-field-box">
                    <label htmlFor="password">
                      Password:
                      <span className="detail-text" id="password1-span"></span>
                      <span
                        className="err-desc"
                        id="password1-description"
                      ></span>
                    </label>
                    <input
                      type="password"
                      name="password1"
                      id="password1"
                      className="password-box reg-input-box"
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="reg-field-box">
                    <label htmlFor="password2">
                      Confirm password:
                      <span className="detail-text" id="password2-span"></span>
                      <span
                        className="err-desc"
                        id="password2-description"
                      ></span>
                    </label>
                    <input
                      type="password"
                      name="password2"
                      id="password2"
                      className="password-box reg-input-box"
                      onChange={handleChange}
                      required
                    />
                  </div>

                  <div className="reg-field-box">
                    <label htmlFor="mfa">
                      Enable two-factor authentication:
                    </label>
                    <input
                      type="checkbox"
                      name="mfa"
                      id="mfa"
                      className="check-box reg-input-box"
                      onChange={handleChange}
                      value="true"
                      required
                    />
                  </div>

                  <input
                    type="submit"
                    value="Register"
                    className="login"
                    id="register-button"
                    onClick={handleSubmit}
                  />

                  {qrFlag && <QRModal data={qrCode} />}
                </div>
              </div>
            </div>
          </div>
        </div>
      </>
    </ThemeProvider>
  );
}

export default RegisterView;
