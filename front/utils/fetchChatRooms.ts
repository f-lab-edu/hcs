import axios from 'axios';

const fetchChatRooms = (url: string) =>
    axios
        .get(url, {
            withCredentials: true,
        })
        .then((response) => response.data.HCS.item.chatRooms);

export default fetchChatRooms;
