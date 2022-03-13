import styled from "@emotion/styled";

type isOwnType = {
    isOwn: boolean;
}

export const Div = styled.div<isOwnType>`
    display: flex;
    flex-direction: column;
    margin-top: 20px;      
    align-items: ${props => props.isOwn ? "flex-end" : "none"};
    
    .messageTop {
        display: flex;
    }
    
    .messageText {
        padding: 10px;
        border-radius: 20px;
        background-color: ${props => props.isOwn ? "#1877f2" : "rgb(245, 241, 241)"};
        color: ${props => props.isOwn ? "white" : "black"};
        max-width: 300px;
    }
    
    .messageBottom {
        font-size: 12px;        
        margin-top: -10px;
    }                             
`;

export const Img = styled.img`    
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
    margin-right: 10px;                                      
`;
