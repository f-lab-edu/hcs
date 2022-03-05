import useInput from '@hooks/useInput';
import React, {useCallback, useState} from 'react';
import {Link} from 'react-router-dom';
import axios from 'axios';

import {
    Button,
    Error,
    Form,
    Header,
    Input,
    Label,
    LinkContainer,
    Success
} from './styles';

const SignUp = () => {

    const [email, onChangeEmail] = useInput('');
    const [nickname, onChangeNickname] = useInput('');
    const [password, , setPassword] = useInput('');
    const [passwordCheck, , setPasswordCheck] = useInput('');
    const [mismatchError, setMismatchError] = useState(false);
    const [signUpError, setSignUpError] = useState('');
    const [signUpSuccess, setSignUpSuccess] = useState(false);

    // 비밀번호를 바꿀 경우
    const onChangePassword = useCallback(
        (e) => {
            setPassword(e.target.value);
            setMismatchError(e.target.value !== passwordCheck);
        },
        [passwordCheck],
    );

    // 비밀번호 확인을 바꿀 경우
    const onChangePasswordCheck = useCallback(
        (e) => {
            setPasswordCheck(e.target.value);
            setMismatchError(e.target.value !== passwordCheck);
        },
        [password],
    )

    const onSubmit = useCallback(
        (e) => {
            e.preventDefault();
            if (!mismatchError) {
                axios.post('/api/user/', {
                    email, nickname, password
                })
                    .then((response) => {
                        console.log(response)
                        setSignUpSuccess(true);
                    })
                    .catch((error) => {
                        console.log(error.response)
                        setSignUpError(error.response.data);
                    })
                    .finally(() => {
                    })

            }
        }, [email, nickname, password, passwordCheck, mismatchError]
    )

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
                <Label id="nickname-label">
                    <span>닉네임</span>
                    <div>
                        <Input type="text" id="nickname" name="nickname"
                               value={nickname} onChange={onChangeNickname}/>
                    </div>
                </Label>
                <Label id="password-label">
                    <span>비밀번호</span>
                    <div>
                        <Input type="password" id="password" name="password"
                               value={password} onChange={onChangePassword}/>
                    </div>
                </Label>
                <Label id="password-check-label">
                    <span>비밀번호 확인</span>
                    <div>
                        <Input
                            type="password"
                            id="password-check"
                            name="password-check"
                            value={passwordCheck}
                            onChange={onChangePasswordCheck}
                        />
                    </div>
                    {mismatchError && <Error>비밀번호가 일치하지 않습니다.</Error>}
                    {!nickname && <Error>닉네임을 입력해주세요.</Error>}
                    {signUpError && <Error>{signUpError}</Error>}
                    {signUpSuccess && <Success>회원가입되었습니다! 로그인해주세요.</Success>}
                </Label>
                <Button type="submit">회원가입</Button>
            </Form>
            <LinkContainer>
                이미 회원이신가요?&nbsp;
                <Link to="/login">로그인 하러가기</Link>
            </LinkContainer>
        </div>
    );
}

export default SignUp;
