import styled from "@emotion/styled";

export const Nav = styled.nav`        
        
    position: fixed;
    top: 0;    
    right: 0;
    left: 0;    
    z-index: 1;
    justify-content: center;
    display: flex;
    background-color: white;
    border-bottom: 1px solid #DBDBDB;
    
    .nav-container {
        max-width: 1000px;
        padding: 0 20px;
        display: flex;
        align-items: center;
    }

    .nav-1, .nav-2 {
        width: 370px;
        display: flex;
        align-items: center;
    }        
    
    .nav-2 {
        justify-content: flex-end;
        
        span {
            display: inline;
            text-align: left;
            justify-content: center;
            margin-left: 25px;
            cursor: pointer;
        }  
        
        .fa-stack-1x {
            top: 6px;
        }
    }                            
`;

export const Input = styled.input`

    width: 200px;
    height: 12px;
    padding: 7px;
    box-sizing: content-box;
    background-color: #fafafa;
    border: 1px solid #DBDBDB;
    border-radius: 3px;
    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABmJLR0QA/wD/AP+gvaeTAAACI0lEQVRIie2Uv2sTcRjGP+/lKqZNLUVrp2wq/mhBJxddWnF0SOsVEhpwKEUCgv9A5zgLocGtUq+cQZCMWg/r5FAQWkFXaatEEfxRhFzu+zqkwZL2rqeufZY7eO95Pt/vA/fCoQ6QRA08z0sHoc4IMqlwDugHNgVZVqPVQsFZ/WeA69YuG8xjIBvhU5DKz+9f787OzgZxACsi3AeyCq8Vcq2mfWJwIHNU1BpRpQz8Ai31Hxt8pKqRLey5QbsW3gNZFa0esaTkOE7YbVpc9C5i8Rw4LiqlfP5mJdENglBnOiePCgcoFJw3onoLQEXnfN+3EwEEmdx5vRcV3lE+P1VHWAOGtxqNq4kACucBwqa9Ehf+xyArO88LiQBAH4Ax37YT5av+ABDIJAVsAaTS6VNJACKcaYPYTAYQfQFgGatwULjneUPAdcCEKV4mA4QyD6gId1y3dinKpKoShNynXU296DgfEgHav79UgF6DefbQ9W7sd/LFpdoSMAWgYspR4bDPqqhWqz2ZgUEXZWLnizVUVkTMNiqntV1L3y7L2zCwx4rFXCMRANoVuG7ttorOAcNdYwPUVUzZUuuBwkgcJHaP+L5vb3z6ciWljKqYXsTaCC191el8YeHJSbuntawwIrDeCuzxbkgsIIk8zxsKDMsoo8A700qNTU9PfOzM92zTv5XjOJ/Dpn1NYB04a9nh093z/wYAFIu5RiuwxxFWgdgddqg9+g0TJuLzCuoJngAAAABJRU5ErkJggg==");
    background-size: 14px;
    background-repeat: no-repeat;
    background-position: 38% center;
    text-indent: 10%;
    font-size: 14px;
    
    &:focus {
        background-position: 4% center;
        outline: none;
    }
    
    &:input:focus::placeholder {
        text-align: start;
    }
    
    &::placeholder, .input-search:not(:focus) {
        text-align: center;
        vertical-align: center;
        font-weight: 300;
        font-size: 14px;
        color: #8e8e8e;
    }    
    
    &::-webkit-search-cancel-button {
        -webkit-appearance: none;
        height: 14px;
        width: 14px;
        background-repeat: no-repeat;
        background-size: 13px;
        background-position: center left;        
    }
`;
