import Cookies from "universal-cookie";

const cookies = new Cookies();

export const getCookie = (name) => {
    return cookies.get(name);
}

export const removeCookie = (name) => {
    return cookies.remove(name);
}

