import axios from 'axios';

const fetchByEmail = (email: string) =>

    axios
        .get(`/api/user/info?email=${email}`, {
            withCredentials: true,
        })
        .then((response) => response.data.HCS.item.profile);

export default fetchByEmail;
