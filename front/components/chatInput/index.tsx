import {
    ChatArea,
    Form,
    MentionsTextarea,
    SendButton,
    Toolbox
} from '@components/chatInput/styles';
import {IChatUser} from '@typings/db';
import React, {useEffect, useRef, VFC} from 'react';
import autosize from 'autosize';

interface Props {
    chat: string;
    friend: IChatUser | undefined;
    onSubmitForm: (e: any) => void;
    onChangeChat: (e: any) => void;
    placeholder?: string;
}

const ChatInput: VFC<Props> = ({
                                   chat,
                                   friend,
                                   onSubmitForm,
                                   onChangeChat,
                                   placeholder
                               }) => {

    const textareaRef = useRef<HTMLTextAreaElement>(null);
    useEffect(() => {
        if (textareaRef.current) {
            autosize(textareaRef.current);
        }
    }, []);

    return (
        <ChatArea>
            <Form onSubmit={onSubmitForm}>
                <MentionsTextarea
                    id="editor-chat"
                    value={chat}
                    onChange={onChangeChat}
                    placeholder={placeholder}
                    ref={textareaRef}
                />
                <Toolbox>
                    <SendButton
                        className={
                            'c-button-unstyled c-icon_button c-icon_button--light c-icon_button--size_medium c-texty_input__button c-texty_input__button--send' +
                            (chat?.trim() ? '' : ' c-texty_input__button--disabled')
                        }
                        data-qa="texty_send_button"
                        aria-label="Send message"
                        data-sk="tooltip_parent"
                        type="submit"
                        disabled={!chat?.trim()}
                    >
                        <i className="c-icon c-icon--paperplane-filled"
                           aria-hidden="true"/>
                    </SendButton>
                </Toolbox>
            </Form>
        </ChatArea>
    );
};

export default ChatInput;