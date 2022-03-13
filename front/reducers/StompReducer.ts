import {
    STOMP_GET_FAILURE,
    STOMP_GET_SUCCESS,
    STOMP_SAVE,
    StompType
} from "@actions/StompActionTypes";

interface InitialState {
    success: boolean
    stomp?: StompType
}

const initialState: InitialState = {
    success: false
}

const StompReducer = (state = initialState, action: any) => {
    switch (action.type) {

        case STOMP_SAVE:
            return {
                success: true,
                stomp: action.payload
            }

        case STOMP_GET_SUCCESS:
            const stomp = action.payload
            return {
                success: true,
                stomp: stomp
            }

        case STOMP_GET_FAILURE:
            return {
                success: false
            }

        default:
            return state;
    }
}

export default StompReducer;
