import React from "react";
import {IconButton, Link} from "@material-ui/core";

require('dotenv').config()

const loginUri = `https://localhost/oauth2/authorization/kakao`;

const KakaoLogin = () => {

    const styles = {
        button: {
            left: 45,
        }
    };

    return (
        <IconButton style={styles.button}>
            <Link href={loginUri} rel="noopener noreferrer">
                <img
                    src={`${process.env.REACT_APP_PUBLIC_FOLDER}/kakao_login_medium_wide.png`}/>
            </Link>
        </IconButton>
    );
};

export default KakaoLogin;
