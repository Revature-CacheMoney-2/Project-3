import React, { useState } from "react";
import "./Modal.css";
import { Link } from "react-router-dom";

export default function QRModal(props) {
  const [modal, setModal] = useState(true);

  const toggleModal = () => {
    setModal(!modal);
  };

  if (modal) {
    document.body.classList.add("active-modal");
  } else {
    document.body.classList.remove("active-modal");
  }

  return (
    <>
      {modal && (
        <div className="modal">
          <div className="overlay"></div>
          <div className="modal-content">
            <h2>Scan QR Code</h2>
            <p>
              To scan, use your authenticator app (Google Authenticator, Authy,
              Microsoft Authenticator)
            </p>

            <img src={props.data} className="qr-image" />
            <br />

            <Link to={"/signin"}>
              <button className="btn-modal">Go to Signin</button>
            </Link>
          </div>
        </div>
      )}
    </>
  );
}
