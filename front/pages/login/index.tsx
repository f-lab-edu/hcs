import useInput from '@hooks/useInput';
import {
    Button,
    Error,
    Form,
    Header,
    Input,
    Label,
    LinkContainer
} from '@pages/signup/styles';
import axios from 'axios';
import React, {useCallback, useState} from 'react';
import {Link, Redirect} from 'react-router-dom';
import useSWR from "swr";
import fetchByEmail from "@utils/fetchByEmail";
import {IChatUser} from "@typings/db";
import KakaoLogin from "@components/kakaoLogin";
import GoogleLogin from "@components/googleLogin";
import NaverLogin from "@components/naverLogin";
import FacebookLogin from "@components/facebookLogin";

const LogIn = () => {

    const [email, onChangeEmail] = useInput(' ');
    const [password, onChangePassword] = useInput('');
    const [logInError, setLogInError] = useState(false);
    const {
        data: userData,
        error,
        revalidate,
        mutate
    } = useSWR<IChatUser | false>(email, fetchByEmail, {
        revalidateOnMount: false,
        revalidateOnFocus: false,
        onSuccess() {
            localStorage.setItem("email", email)
        }
    });
    const onSubmit = useCallback(
        (e) => {
            e.preventDefault();
            setLogInError(false);
            let bodyFormData = new FormData();
            bodyFormData.append("email", email)
            bodyFormData.append("password", password)
            axios({
                method: 'post',
                url: '/api/login',
                headers: {'Content-Type': 'multipart/form-data'},
                data: bodyFormData,
                withCredentials: true,
            })
                .then((response) => {
                    revalidate()
                    console.log("login success!")
                })
                .catch((error) => {
                    // @ts-ignore
                    console.log(error.response.status);
                    setLogInError(error.response.status === 401)
                });
        },
        [email, password],
    );

    if (userData) {
        console.log("redirect to messenger");
        return <Redirect to="/messenger"/>;
    }

    return (
        <div id="container">
            <Header>HCS</Header>
            <Form onSubmit={onSubmit}>
                <Label id="email-label">
                    <span>이메일 주소</span>
                    <div>
                        <Input type="email" id="email" name="email"
                               value={email} onChange={onChangeEmail}/>
                    </div>
                </Label>
                <Label id="password-label">
                    <span>비밀번호</span>
                    <div>
                        <Input type="password" id="password" name="password"
                               value={password} onChange={onChangePassword}/>
                    </div>
                    {logInError && <Error>이메일과 비밀번호 조합이 일치하지 않습니다.</Error>}
                </Label>
                <Button type="submit">로그인</Button>
                <KakaoLogin/>
                <GoogleLogin/>
                <NaverLogin/>
                <FacebookLogin/>
            </Form>
            <LinkContainer>
                아직 회원이 아니신가요?&nbsp;
                <Link to="/signup">회원가입 하러가기</Link>
            </LinkContainer>
        </div>
    );
};

export default LogIn;
