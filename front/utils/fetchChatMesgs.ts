import axios from 'axios';

const fetchChatMesgs = (url: string) =>
    axios
        .get(url, {
            withCredentials: true,
        })
        .then((response) => response.data.HCS.item.latestChatMessages);

export default fetchChatMesgs;
