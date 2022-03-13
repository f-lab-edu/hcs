import axios from 'axios';

const fetchById = (userId: number) =>

    axios
        .get(`/api/user/info/?userId=${userId}`, {
            withCredentials: true,
        })
        .then((response) => response.data.HCS.item.profile);

export default fetchById;
