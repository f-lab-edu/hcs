import React, {FC} from "react"
import {NavLink} from 'react-router-dom';
import {Div} from '@components/chatMenu/styles';
import {IChatRoom, IChatUser} from '@typings/db';
import useSWR from 'swr';
import Conversation from "@components/conversation";
import fetchByEmail from "@utils/fetchByEmail";
import fetchChatRooms from "@utils/fetchChatRooms";

const ChatMenu: FC = () => {
    const email = localStorage.getItem("email");
    const {
        data: userData,
        error,
        revalidate,
        mutate
    } = useSWR<IChatUser>(email, fetchByEmail, {
        dedupingInterval: 20000, // 20초
    });

    const {data: chatRoomsList} = useSWR<IChatRoom[]>(
        `/api/chat/room/list?userId=${userData?.userId}`, fetchChatRooms, {
            dedupingInterval: 5000, // 5초
        });

    return (
        <Div>
            <div className="chatMenuWrapper">
                {chatRoomsList?.map(room => {
                    return (
                        <NavLink key={room.chatRoomId}
                                 activeClassName="selected"
                                 to={{
                                     pathname: `/messenger/dm/${room.chatRoomId}`,
                                     state: {chatRoom: room}
                                 }}>
                            <Conversation chatRoom={room}
                                          currentUserId={userData?.userId}/>
                        </NavLink>
                    )
                })}
            </div>
        </Div>
    )
}

export default ChatMenu
