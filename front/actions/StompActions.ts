import {Dispatch} from "react";
import {
    STOMP_GET_FAILURE,
    STOMP_GET_SUCCESS,
    STOMP_SAVE,
    StompDispatchType
} from "./StompActionTypes";

import {Client} from '@stomp/stompjs'

export const fetchStompData = () => (dispatch: Dispatch<StompDispatchType>) => {
    try {
        dispatch({type: STOMP_GET_SUCCESS})

    } catch (err) {
        dispatch({type: STOMP_GET_FAILURE})
    }
}

export const saveStompData = (client: React.MutableRefObject<Client | undefined>): StompDispatchType => ({
    type: STOMP_SAVE,
    payload: client?.current!
})
