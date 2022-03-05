import styled from "@emotion/styled";

export const Div = styled.div`
    flex: 5.5;                 
    
    .chatBoxWrapper {
        height: 90%;
        display: flex;
        overflow-y: auto;
        flex-direction: column;
        justify-content: space-between;
        position: relative;         
        
        .chatBoxTop {
            height: 100%;
            overflow-y: auto;
            padding-right: 10px;
        }
        
        .chatBoxBottom {
            margin-top: 5px;
            display: flex;
            align-items: center;
            justify-content: space-between;            
        }
    }                                          
`;

export const TextArea = styled.textarea`
    width: 80%;
    height: 90px;
    padding: 10px;
`;

export const Button = styled.button`
    width: 70px;
    height: 40px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    background-color: teal;
    color: white;
`;

export const NoConversationText = styled.span`
    position: absolute;
    top: 10%;
    font-size: 50px;
    color: rgb(224, 220, 220);
    cursor: default;
`;