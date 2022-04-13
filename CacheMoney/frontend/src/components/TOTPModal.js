import React, { useState } from "react";
import userStore from "../store/Store.js";
import config from "../config.js";
import axios from "axios";
import "./Modal.css";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import OTPInput from "otp-input-react";
import { Link } from "react-router-dom";

export default function TOTPModal(props) {
  const navigate = useNavigate();
  const [modal, setModal] = useState(true);

  const [OTP, setOTP] = useState("");

  const toggleModal = () => {
    setModal(!modal);
    window.location.reload(false);
  };

  if (modal) {
    document.body.classList.add("active-modal");
  } else {
    document.body.classList.remove("active-modal");
  }

  const [formData, setFormData] = useState({
    userId: props.data.userId,
    code: "",
  });

  const handleVerify = () => {
    doVerify();
  };

  // const handleChange = (event) => {
  //   event.preventDefault();
  //   setFormData({ ...formData, [event.target.name]: event.target.value });
  //   //
  // };

  function doVerify() {
    let responseStatus;
    let responseData;
    let responseHeaders;
    const url = config.url;
    axios({
      method: "post",
      url: `${url}users/verify`,
      data: { userId: formData.userId, code: OTP },
    })
      .then((response) => {
        responseData = response.data;
        responseStatus = response.status;
        responseHeaders = response.headers;
        if (responseStatus !== 200 || responseData.user_id === null) {
          console.log("Verify failed - bad TOTP TOTP");
          document.getElementById("login-error-box").innerHTML =
            "Error: invalid TOTP code.";
          let loginInputArr = document.getElementsByClassName("login-input");
          for (let i = 0; i < loginInputArr.length; i++) {
            loginInputArr[i].style.border = "2px solid red";
            loginInputArr[i].style.boxShadow = "-4px 4px 0px #b04050";
          }
          //alert("Invalid login attempt.");
        } else {
          console.log("Response body from login API: ", responseHeaders);

          userStore.dispatch({
            type: "UPDATE_TOKEN",
            payload: responseHeaders.jwt,
          });

          navigate("/main");
        }
      })
      .catch((error) => {
        //console.log(error.response);
        console.error(`Error: ${error}`);
        toast.error(error.response.data, {
          position: "bottom-center",
          autoClose: 2000,
          hideProgressBar: true,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
        });
      });
  }

  return (
    <>
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
      {modal && (
        <div className="modal">
          <div className="overlay"></div>
          <div className="modal-content">
            <h2></h2>
            <span id="login-error-box"></span>

            <h3>Two-factor authentication</h3>

            <p className="small-font">
              Open the two-factor authentication app on your device to view your
              authentication code and verify your identity.

            <strong className="small-font">Authentication code</strong>
            <OTPInput
              value={OTP}
              onChange={setOTP}
              autoFocus
              OTPLength={6}
              otpType="number"
              disabled={false}
              className="otp-input"
            />

            <button
              className="btn-modal"
              id="verify-button"
              type="submit"
              onClick={handleVerify}
            >
              VERIFY
            </button>

            <button className="small-font" onClick={toggleModal}>
              Cancel log in
            </button>
          </div>
        </div>
      )}
    </>
  );
}
