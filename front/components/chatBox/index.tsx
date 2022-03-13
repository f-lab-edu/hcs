import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Div} from '@components/chatBox/styles';
import {useParams} from "react-router";
import {useLocation} from "react-router-dom";
import useSWR from "swr";
import {IChatMessage, IChatUser} from "@typings/db";
import fetchByEmail from "@utils/fetchByEmail";
import useInput from "@hooks/useInput";
import Message from "@components/message";
import ChatInput from "@components/chatInput";
import axios from "axios";
import {useDispatch, useSelector} from "react-redux";
import {RootReducerType} from "@store/index";
import {Client} from "@stomp/stompjs";
import {fetchStompData} from "@actions/StompActions";

const ChatBox = () => {
    const stompReducer = useSelector((state: RootReducerType) => state.StompReducer)
    const dispatch = useDispatch()
    const {roomId} = useParams<{ roomId?: string }>();
    const chatRoomData = useLocation<any>().state.chatRoom
    const email = localStorage.getItem("email");
    const {
        data: userData,
        error,
        revalidate,
        mutate
    } = useSWR<IChatUser>(email, fetchByEmail, {
        dedupingInterval: 20000, // 20초
    });

    const friend: IChatUser = chatRoomData?.chatRoomMembers.find((m: IChatUser) => m.userId !== userData?.userId)!;
    const [chatList, setChatList] = useState<IChatMessage[]>([])
    const [chat, onChangeChat, setChat] = useInput('');

    const fetchChatMesgs = useCallback(() => {
            const chatListData = axios.get(
                `/api/chat/room/?roomId=${roomId}`
            )
            chatListData.then((response) => setChatList(response.data.HCS.item.latestChatMessages))
        }, []
    )

    const client = useRef<Client>();

    useEffect(() => {
        dispatch(fetchStompData())
        if (stompReducer.success) {
            console.log("dispatch for getting client is success! ")
            client.current = stompReducer.stomp
            client.current?.subscribe('/sub/chat/room/' + chatRoomData.chatRoomId, ({body}) => {
                setChatList((_chatList: IChatMessage[]) => [..._chatList, JSON.parse(body)])
            })
        }
        fetchChatMesgs()
    }, []);

    const onSubmitForm = useCallback(
        (e) => {
            e.preventDefault();
            console.log(chat);
            if (chat?.trim()) {
                client.current?.publish({
                    destination: '/pub/chat/message',
                    body: JSON.stringify({
                        roomId: roomId,
                        authorId: userData?.userId,
                        message: chat
                    })
                })
                setChat('');
            }
        },
        [chat, chatList, userData, friend, roomId]
    );

    return (
        <Div>
            <div className="chatBoxWrapper">
                <div className="chatBoxTop">
                    {chatList && chatList.map((chat: IChatMessage, index) => {
                        if (chat.authorId === userData?.userId) {
                            return <Message key={index} isOwn
                                            chatMessage={chat}
                                            me={userData}/>;
                        } else {
                            return <Message key={index}
                                            chatMessage={chat} me={friend}/>;
                        }
                    })}
                </div>
                <div className="chatBoxBottom">
                    <ChatInput chat={chat} friend={friend}
                               onChangeChat={onChangeChat}
                               onSubmitForm={onSubmitForm}/>
                </div>
            </div>
        </Div>
    );
};

export default ChatBox
