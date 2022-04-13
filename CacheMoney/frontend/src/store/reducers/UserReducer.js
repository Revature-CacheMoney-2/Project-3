/**
 * @Author Cody Gonsowski, Jeffrey Lor
 */

const initialState = {
	userId: "",
	username: "",
	firstName: "",
	lastName: "",
	token: "",
	mfa: false
};


function userReducer(state = initialState, action) {
	switch (action.type) {
		case "LOGOUT_USER":
			return {
				...initialState
			};

		case "UPDATE_ID":
			return {
				...state,
				userId: action.payload
			};

		case "UPDATE_USERNAME":
			return {
				...state,
				username: action.payload
			};

		case "UPDATE_NAME_FIRST":
			return {
				...state,
				firstName: action.payload
			};

		case "UPDATE_NAME_LAST":
			return {
				...state,
				lastName: action.payload
			};

		case "UPDATE_TOKEN":
			return {
				...state,
				token: action.payload
			};
		
		case "UPDATE_MFA":
			return {
				...state,
				mfa: action.payload
			};

		default:
			return state;
	}
}

export default userReducer;