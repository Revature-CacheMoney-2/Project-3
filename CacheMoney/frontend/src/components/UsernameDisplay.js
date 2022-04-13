import axios from 'axios'
import { useRef, useState } from 'react'
import config from '../config'
import store from '../store/Store.js'
import { ToastContainer, toast } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css';
function UsernameDisplay(){

    const [userName, setUserName] = useState(
        store.getState().userReducer.username
      )
      const securityMessage =
        "note: For added security, please choose a username that's unique."
      const [warning, setWarning] = useState('')
      const newUserName = useRef(null)

      // retrieve the url from the config
      const url = config.url

      const postAccount = () => {
        let user = {
            username: newUserName.current.value
        }

        const token = store.getState().userReducer.token
        const userId = store.getState().userReducer.userId

        const headers = {'Content-Type': 'application/json', 'token': token,
                'userId': userId}

        axios.patch(`${url}users`, user, {headers: headers})
        .then(response => {
          const status = response.status
          const data = response.data
          if(status===200){
            if(data ){
              setUserName(newUserName.current.value)
              toast.success('Username has been successfully updated', {
              position: "top-center",
              autoClose: 5000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
              progress: undefined,
              });
              setWarning("")
            }else{
            toast.error('Username could not be updated', {
                                position: "top-center",
                                autoClose: 5000,
                                hideProgressBar: false,
                                closeOnClick: true,
                                pauseOnHover: true,
                                draggable: true,
                                progress: undefined,
                                });
              setWarning("An error saving the username.")
            }
          }

        }).catch(err => console.log(err))
      }

      // what the submit button should do
      const handleSubmit = () => {
        //compare old username to new username and
        if (userName === newUserName.current.value) {
          setWarning('error: old username must not match new username')
        }else if((newUserName.current.value.length <5)){
          setWarning('error: username must be more than 5 characters long.')
        } else {
          store.dispatch({
            type: 'UPDATE_USERNAME',
            payload: newUserName.current.value,
          })
          //send patch request to the database
          postAccount()
          setWarning('')
        }
      }
    return (
        <div className="create-account-inner-container">
                <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover/>
                <div className="account_create_form">
                  <p className="account_create_form_header">Update UserName</p>

                  <label  id='errorMessage'>{warning}</label>
                  <form>
                    <div className="account_create_name">

                      <label htmlFor="old_username">Old UserName</label>
                      <input
                        type="text"
                        value={userName}
                        readOnly
                      />
                    </div>
                    <div className="account_create_name">
                      <label htmlFor="account_name">New UserName</label>
                      <input
                        type="text"
                        ref={newUserName}
                      />
                    </div>
                  </form>

                  <button
                    className="account_create_submit_button"
                    type="button"
                    name="submit"
                    id="create-new-account"
                    onClick={handleSubmit}
                  >
                    Submit
                  </button>
                  <label id='securityMessage'>{securityMessage}</label>
                </div>
              </div>
    )
}

export default UsernameDisplay;
