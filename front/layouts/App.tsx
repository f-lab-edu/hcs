import React from 'react'
import loadable from '@loadable/component';
import {Redirect, Route, Switch} from "react-router-dom";
import {Provider} from "react-redux";
import store from "@store/index";

const Messenger = loadable(() => import('@layouts/messenger'));
const SignUp = loadable(() => import('@pages/signup'));
const LogIn = loadable(() => import('@pages/login'));

const App = () => {
    return (
        <React.StrictMode>
            <Provider store={store}>
                <Switch>
                    <Redirect exact path="/" to="/login"/>
                    <Route path="/login" component={LogIn}/>
                    <Route path="/signup" component={SignUp}/>
                    <Route path="/messenger" component={Messenger}/>
                </Switch>
            </Provider>
        </React.StrictMode>
    );
};

export default App;
