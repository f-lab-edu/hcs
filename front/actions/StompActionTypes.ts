import {Client} from "@stomp/stompjs";

export const STOMP_GET_SUCCESS = 'STOMP_SUCCESS'
export const STOMP_GET_FAILURE = 'STOMP_FAILURE'
export const STOMP_SAVE = 'STOMP_SAVE'

export type StompType = {
    type: Client
}

export interface saveStomp {
    type: typeof STOMP_SAVE
    payload: Client
}

export interface stompSuccessDispatch {
    type: typeof STOMP_GET_SUCCESS
}

export interface stompFailDispatch {
    type: typeof STOMP_GET_FAILURE
}

export type StompDispatchType =
    saveStomp
    | stompFailDispatch
    | stompSuccessDispatch;
