/**
 * @author Cody Gonsowski & Jeffrey Lor
 */
import axios from "axios";
import config from "../../config";
import store from "../../store/Store";
import TransferSelection from "./TransferSelection";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function Transfer(props) {
	const notify=()=>{
		toast("")
	}

	// post transfer transaction
	const postTransfer = (transaction) => {
		
		axios
			.post(`${config.url}accounts/transfer`, transaction, {
				headers: {
					token: store.getState().userReducer.token,
					userId: store.getState().userReducer.userId,
				},
			})
			.then(
				res=>{
				res.status==200?
					toast.success('Transfer successful', {
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
				
				toast.error('Transfer failed', {
						position: "bottom-right",
						autoClose: 2000,
						hideProgressBar: true,
						closeOnClick: true,
						pauseOnHover: true,
						draggable: true,
						progress: undefined,
						})
					}
			);

			
	};

	// what the submit button should do
	const handleSubmit = (event) => {
		// prevent page reloading
		event.preventDefault();

		// create the transfer payload
		let transfer = {
			sourceAccountId: store.getState().transferReducer.sourceAccountId,
			destinationAccountId:
				store.getState().transferReducer.destinationAccountId,
			transaction: {
				description: event.target.description.value,
				transactionAmount: event.target.transactionAmount.value,
			},
		};

		// perform the post
		postTransfer(transfer);
		
		
		// hacky workaround to try forcing the accounts list to update
		props.doTransactionDone(Date.now());
	};

	return (
		<div className="transfer-outer-container">
			<ToastContainer />
			<div className="transfer-inner-container">
				<div className="transfer-form">
					<p className="transfer-form-header">Transfer</p>

					<form id="transfer-inner-form" onSubmit={handleSubmit}>
						<div className="transfer-from-account">
							<label>From</label>
							<TransferSelection whichAccount="SOURCE"></TransferSelection>
						</div>

						<div className="transfer-to-account">
							<label>To</label>
							<TransferSelection whichAccount="DESTINATION"></TransferSelection>
						</div>

						<div className="transfer-amount">
							<label>Amount</label>
							<input
								type="number"
								min="0.00"
								step="0.01"
								id="transfer-input"
								name="transactionAmount"
							/>
						</div>

						<div className="transfer-description">
							<label>Description</label>
							<input type="text" id="transfer-description" name="description" />
						</div>

						<button type="submit">Submit</button>
					</form>
				</div>
			</div>
		</div>
	);
}

export default Transfer;
