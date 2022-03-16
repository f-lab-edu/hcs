import {Client} from "@stomp/stompjs";
import SockJS from "sockjs-client";

const useStomp = (client: React.MutableRefObject<Client | undefined>, destination: string, callback) => {
    const connect = () => {
        client.current = new Client({

            webSocketFactory: () => new SockJS('/ws-stomp', {}, {
                transports: ["xhr-polling"]
            }),
            reconnectDelay: 200000,
            heartbeatIncoming: 16000,
            heartbeatOutgoing: 16000,
            connectHeaders: {
                login: 'guest',
                passcode: 'guest',
            },
            onConnect: () => {
                console.error("0 stomp onConnect : ");
                client.current?.subscribe(destination, callback)
            },
            onStompError: (frame) => {
                console.error("1 stomp error : ", frame);
            },
            onDisconnect: (frame) => {
                console.error("2 disconnect : ", frame);
            },
            onWebSocketClose: (frame) => {
                console.log("3 Stomp WebSocket Closed", frame);
            },
            debug: function (str) {
                console.error("4 debug : ", str);
            },
            onUnhandledMessage: (msg) => {
                console.log("5 unhandled Message", msg);
            }
        });

        client.current?.activate();
    };

    const disconnect = () => {
        client.current?.deactivate();
    };

    return [connect, disconnect]
}

export default useStomp;
