import React, {FC} from 'react';
import {Div, Img} from '@components/message/styles';
import {IChatMessage, IChatUser} from "@typings/db";

interface Props {
    isOwn?: boolean;
    chatMessage?: IChatMessage
    me: IChatUser
}

const Message: FC<Props> = ({isOwn = false, chatMessage, me}) => {

    const PF: string | undefined = process.env.REACT_APP_PUBLIC_FOLDER

    return (
        <Div className="message" isOwn={isOwn}>
            <div className="messageTop">
                <Img
                    className="conversationImg"
                    src={me?.profileImage ? PF + "person/noAvatar.png" : me?.profileImage}
                />
                <p className="messageText">{chatMessage?.message}</p>
            </div>
        </Div>
    )
}

export default Message
