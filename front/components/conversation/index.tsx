import React, {FC, useEffect, useState} from "react"
import {Div, Img} from '@components/conversation/styles';
import {IChatRoom, IChatUser} from "@typings/db";
import axios from "axios";

interface Props {
    chatRoom: IChatRoom;
    currentUserId?: number;
}

const Conversation: FC<Props> = ({chatRoom, currentUserId}) => {

    const [user, setUser] = useState<IChatUser>()
    const PF: string | undefined = process.env.REACT_APP_PUBLIC_FOLDER
    const friend: IChatUser | undefined = chatRoom.chatRoomMembers.find((m: IChatUser) => m.userId !== currentUserId);
    const friendId = friend?.userId

    useEffect(() => {
        const getUser = async () => {
            try {
                const friend = await axios("/api/user/?userId=" + friendId);
                setUser(friend.data.HCS.item.profile);
            } catch (err) {
                console.log(err)
            }
        }
        getUser();
    }, [currentUserId, chatRoom])

    return (
        <Div className="conversation">
            <Img
                className="conversationImg"
                src={user?.profileImage ? PF + "person/noAvatar.png" : user?.profileImage}
            />
            <div className="conversationContent">
                <div className="conversationName">{user?.nickname}</div>
                <div
                    className="lastMessage">{chatRoom.lastChatMesg.message}</div>
            </div>
        </Div>
    )
}

export default Conversation
