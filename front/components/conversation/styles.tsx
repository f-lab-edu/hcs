import styled from "@emotion/styled";

export const Div = styled.div`    
    display: flex;           
    align-items: center;
    padding: 10px;    
                
    .conversationContent {
        .conversationName {
            font-weight: 500;    
            font-size: 18px;    
        }                           
    
        .lastMessage {
            font-weight: 300;
            font-size: 14px;
            margin-top: 3px;
        }
    }                            
`;

export const Img = styled.img`
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
    margin-right: 20px;                                 
`;
