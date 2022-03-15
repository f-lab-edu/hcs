import React from "react";
import { FacebookLoginButton } from "react-social-login-buttons";

require('dotenv').config()

 const loginUri = `/api/oauth2/authorization/facebook`


const FacebookLogin = () => {

    return (
        < a href={`${loginUri}`}>
            <FacebookLoginButton />
        </a>
    );
};

export default FacebookLogin;
