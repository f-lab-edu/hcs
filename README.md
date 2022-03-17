# Hobby Community Service

### 1. 서비스 소개
- 당근마켓을 클론한 프로젝트입니다.
- 내가 사는 동네에 함께 사는 주민들을 위한 플랫폼 서비스 입니다
- 활발하고 편리한 소통을 위해 다양한 기능을 제공합니다
- 자신의 다양한 취미를 함께 즐길 수 있는 동호회를 찾거나 생성할 수 있습니다
- 다이렉트 메시지를 통해 사용자끼리 채팅 및 중고거래를 할 수 있습니다


## HCS 프로젝트 구성도

<img width="718" alt="스크린샷 2022-03-17 오후 1 47 28" src="https://user-images.githubusercontent.com/58963724/158739404-b14b3008-a2d7-461f-ac2c-165d9b3d56cc.png">

## ERD

<img width="541" alt="스크린샷 2022-03-17 오후 2 15 14" src="https://user-images.githubusercontent.com/58963724/158741892-cc641980-92f2-4391-8284-b9d86d3e2404.png">

## 01. User Flow
1. Form Login

### 로그인 버튼을 누를 시 인증 과정을 거침
### 로그인 성공

![ezgif com-gif-maker](https://user-images.githubusercontent.com/58963724/158743791-d1fa1e37-48cc-4435-b7f7-e4a16052d513.gif)



### 로그인 실패

![log fail](https://user-images.githubusercontent.com/58963724/158743743-44d1521b-e820-41ac-88d3-a6912f9c70cc.gif)


### 2. OAuth2.0 Login

1. Google

![google oauth](https://user-images.githubusercontent.com/58963724/158743769-b1d57529-f57d-4736-a5e4-84bbec66ffd1.gif)


2. Facebook

![facebook](https://user-images.githubusercontent.com/58963724/158751945-b2ce8dee-19a7-4e95-b8b2-9d3704c30118.gif)


3. Kakao

![oauth kakao login](https://user-images.githubusercontent.com/58963724/158742975-e2bd243b-d7a2-4482-809b-5467c679d294.gif)


4. Naver

![naver](https://user-images.githubusercontent.com/58963724/158751971-5fa7a245-3911-453d-a26b-9399bcddad48.gif)


## 3. 채팅하기
### 1. /messenger

### 사용자가 로그인시 자신의 채팅방 목록이 왼쪽 사이드바에 나타납니다
<img width="538" alt="스크린샷 2022-03-17 오후 2 46 47" src="https://user-images.githubusercontent.com/58963724/158745033-61512bd4-af65-4fde-b91b-195d0cbc4545.png">


### 원하는 방을 누르고 접속하면 채팅 목록들이 나오게됩니다
![chat1](https://user-images.githubusercontent.com/58963724/158745258-2430b346-b37e-4997-9b87-2801f885c236.gif)


### 채팅 전송을 눌러 실시간으로 채팅이 가능합니다
### 채팅방의 최신 메시지가 업데이트 됩니다
![chat2](https://user-images.githubusercontent.com/58963724/158745271-4a1d5a07-4710-47de-ab62-394c42c1c84b.gif)


### 채팅이 오게 될경우 상단 네비게이션 바에 알람이 도착합니다
![chat alarm](https://user-images.githubusercontent.com/58963724/158744907-38619f0a-b64e-4f39-ab3d-053b9df45f6c.gif)


## 02. 기술스택
| 카테고리 | 기술 |
| --- | --- |
| Language | Java,Javascript |
| Framework | SpringBoot |
| ORM | JPA,MyBatis |
| Security | Spring Security |
| Messaging Protocol | STOMP |
| Build Tool | Gradle,Jenkins |
| DB | MySQL,Redis |
| Test | Mockito,JUnit |
| Front | React,Typescript,Redux,SWR |
|  |  |
 
