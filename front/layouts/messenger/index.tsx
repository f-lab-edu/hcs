import React, {VFC} from 'react';
import {Div} from '@layouts/messenger/styles';
import loadable from "@loadable/component";
import {Route} from "react-router-dom";

const Navigation = loadable(() => import('@components/navigation'));
const ChatMenu = loadable(() => import('@components/chatMenu'));
const ChatBox = loadable(() => import('@components/chatBox'));

const Messenger: VFC = () => {

    return (
        <>
            <Div>
                <Navigation/>
                <ChatMenu/>
                <Route exact path="/messenger/dm/:roomId"
                       component={ChatBox}/>
            </Div>
        </>
    )
}

export default Messenger;
