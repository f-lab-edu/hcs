import React, {useCallback, useEffect, useRef, useState} from 'react';
import {Input, Nav} from '@components/navigation/styles';
import {
    FaFacebookMessenger,
    FaHome,
    FaRegCircle,
    FaRegHeart,
    FaRegUser,
    FaSatellite,
} from "react-icons/fa";
import {BiLogOut} from "react-icons/bi";
import axios from "axios";
import useSWR from "swr";
import fetchByEmail from "@utils/fetchByEmail";
import {IChatUser} from "@typings/db";
import {Redirect} from "react-router-dom";
import {Client} from "@stomp/stompjs";
import useStomp from "@hooks/useStomp";
import {useHistory} from "react-router";
import {useDispatch} from "react-redux";
import {saveStompData} from "@actions/StompActions"
import {getCookie, removeCookie} from "@utils/Cookie";

const Navigation = () => {

    const email = useRef<string>();

    if (getCookie("email")) {
        email.current = getCookie("email")
    } else if (localStorage.getItem("email")) {
        email.current! = localStorage.getItem("email")!
    }


    const {
        data: userData,
        error,
        revalidate,
        mutate
    } = useSWR<IChatUser>(email.current!, fetchByEmail);


    const dispatch = useDispatch()
    const history = useHistory();
    const [messengerAlarmed, setMessengerAlarmed] = useState(false)

    const onLogout = useCallback(() => {
        axios.post(`/api/logout`, null, {
            withCredentials: true
        })
            .then(() => {
                mutate(undefined, false);
                localStorage.removeItem("email")
                removeCookie("email")
                console.log("logout success");
                console.log("cached user : " + JSON.stringify(userData));
            })
    }, [])

    const onMessengerClick = useCallback(() => {
        setMessengerAlarmed(false)
        console.log("redirect to /messenger");
        history.push('/messenger')
    }, [])

    const subscribeCallback = useCallback(({body}) => {
        console.log("messageAlarm ! " + JSON.stringify(body))
        console.log("current path : " + window.location.pathname)

        if (!window.location.pathname.startsWith("/messenger")) {
            setMessengerAlarmed(true)
        } else {
            console.log("your path is not allowd for messenger notification!")
        }
    }, [])

    const client = useRef<Client>();
    const [connect, disconnect] = useStomp(client, '/sub/messenger/icon/' + userData?.userId, subscribeCallback)

    useEffect(() => {
        connect();
        dispatch(saveStompData(client))
        return () => disconnect();
    }, []);

    if (userData === undefined && getCookie("email") === undefined) {
        console.log("redirect to login");
        return <Redirect to="/login"/>;
    }

    return (
        <Nav>
            <div className="nav-container">
                <div className="nav-1">
                    <FaSatellite/>
                </div>
                <Input className="input-search" id="searchInput"
                       placeholder="검색"
                       type="search"/>
                <div className="nav-2">
                    <span onClick={onLogout}>
                        <BiLogOut>로그아웃</BiLogOut>
                    </span>
                    <span><FaHome/></span>
                    {messengerAlarmed ?
                        <span onClick={onMessengerClick}
                              className="fa-stack fa-sm">
                            <FaRegCircle className="fa-stack-2x" color="red"/>
                            <FaFacebookMessenger className="fa-stack-1x"/>
                        </span>
                        :
                        <span onClick={onMessengerClick}>
                            <FaFacebookMessenger/>
                        </span>
                    }
                    <span><FaRegHeart/></span>
                    <span><FaRegUser/></span>
                </div>
            </div>
        </Nav>
    );
};

export default Navigation;
