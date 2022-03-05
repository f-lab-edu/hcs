export interface IChatUser {
    userId: number;
    nickname: string;
    email: string;
    profileImage: string;
}

export interface IChatMessage {
    // DM 채팅
    chatMessageId: number;
    authorId: number; // 보낸 사람 아이디
    message: string;
    createdAt: Date;
}

export interface IChatRoom {
    chatRoomId: string;
    lastChatMesg: IChatMessage
    chatRoomMembers: IChatUser[]
    latestChatMessages?: IChatMessage[]
    createdAt: Date
}
