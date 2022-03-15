import React from "react";
import GoogleButton from 'react-google-button'

require('dotenv').config()

const loginUri = `/api/oauth2/authorization/google`

const GoogleLogin = () => {

    return (
        <a href={`${loginUri}`}>
            <GoogleButton
                type="light"
            />
        </a>
    );
};

export default GoogleLogin;
