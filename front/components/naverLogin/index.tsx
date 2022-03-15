import React from "react";
import {IconButton, Link} from "@material-ui/core";

require('dotenv').config()

const loginUri = `http://localhost:8080/oauth2/authorization/naver`


const NaverLogin = () => {

    const styles = {
        button: {
            left: 45,
        }
    };

    return (
        <IconButton style={styles.button}>
            <Link href={loginUri} rel="noopener noreferrer">
                <img
                    src={`${process.env.REACT_APP_PUBLIC_FOLDER}/naver_login_button.png`}
                    width={200}/>
            </Link>
        </IconButton>
    );
};

export default NaverLogin;
