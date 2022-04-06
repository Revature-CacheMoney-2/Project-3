/**
 * @author Cody Gonsowski & Jeffrey Lor
 */
import axios from "axios";
import config from "../../config";
import store from "../../store/Store";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Withdraw(props) {
	// post withdraw transaction
	const postWithdraw = (transaction) => {
		axios
			.post(`${config.url}accounts/withdraw`, transaction, {
				headers: {
					token: store.getState().userReducer.token,
					userId: store.getState().userReducer.userId,
				},
			})

			.then(
				result=>{
					result.status===200?
					toast.success('Withdrawal successful', {
						position: "bottom-right",
						autoClose: 2000,
						hideProgressBar: true,
						closeOnClick: true,
						pauseOnHover: true,
						draggable: true,
						progress: undefined,
					}):toast.error('error')
			}
				)

			.catch((error) => {
				console.error(`Error: ${error}`)
				toast.error('Withdrawal failed', {
					position: "bottom-right",
					autoClose: 2000,
					hideProgressBar: true,
					closeOnClick: true,
					pauseOnHover: true,
					draggable: true,
					progress: undefined,
					})
			});
	};

	// what the submit button should do
	const handleSubmit = (event) => {
		// prevent page reloading
		event.preventDefault();

		// create the withdraw payload
		let withdraw = {
			account: {
				accountId: store.getState().accountReducer.currentAccountId,
			},
			transactionAmount: event.target.transactionAmount.value,
		};

		// perform the post
		postWithdraw(withdraw);
		// maybe do this in/after the api call
		// hacky workaround to try forcing the accounts list to update
		props.doTransactionDone(Date.now());
	};

	return (
		<div className="withdraw-outer-container">
			<ToastContainer />
			<div className="withdraw-inner-container">
				<div className="withdraw-form">
					<p className="withdraw-form-header">Withdraw</p>

					<form id="withdraw-inner-form" onSubmit={handleSubmit}>
						<div className="withdraw-amount">
							<label>Amount</label>
							<input
								type="number"
								min="0.01"
								step="0.01"
								id="withdraw-input"
								name="transactionAmount"
							/>
						</div>

						<button type="submit">Submit</button>
					</form>
				</div>
			</div>
		</div>
	);
}

export default Withdraw;
