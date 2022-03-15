package com.hcs.controller;

import com.hcs.annotation.EnableMockMvc;
import com.hcs.common.JdbcTemplateHelper;
import com.hcs.common.TestSecurityConfig;
import com.hcs.domain.ChatRoom;
import com.hcs.service.ChatMessageService;
import com.hcs.service.ChatRoomService;
import com.hcs.service.TradePostService;
import com.hcs.service.UserService;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSecurityConfig.class)
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
public class ChatRoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;
    @Autowired
    TradePostService tradePostService;
    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    ChatMessageService chatMessageService;

    @Autowired
    JdbcTemplateHelper jdbcTemplateHelper;

    @DisplayName("개인 DM 채팅방 생성 - 입력값 정상")
    @Test
    void createPersonalChatRoom() throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long roomMakerId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);

        String newEmail2 = "test2@naver.com";
        String newNickname2 = "test2";
        String newPassword2 = "password2";
        LocalDateTime joinedAt2 = LocalDateTime.now();

        long guestId = jdbcTemplateHelper.insertTestUser(newEmail2, newNickname2, newPassword2, joinedAt2);

        MvcResult mvcResult = mockMvc.perform(post("/chat/room/personal")
                        .param("roomMakerId", String.valueOf(roomMakerId))
                        .param("guestId", String.valueOf(guestId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        String chatRoomId = (String) item.get("chatRoomId");
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);

        assertThat(chatRoom).isNotNull();
        assertThat(item.get("roomMakerId")).isEqualTo((int) roomMakerId);
        assertThat(item.get("guestId")).isEqualTo((int) guestId);
    }

    @DisplayName("중고거래 시 사용될 채팅방 생성 - 입력값 정상")
    @Test
    void createTradeChatRoomTest() throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long buyerId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);

        String newEmail2 = "test@naver.com";
        String newNickname2 = "test";
        String newPassword2 = "password";
        LocalDateTime joinedAt2 = LocalDateTime.now();

        long sellerId = jdbcTemplateHelper.insertTestUser(newEmail2, newNickname2, newPassword2, joinedAt2);
        String title = "test";
        String productStatus = "중";
        String category = "중";
        String description = "중";
        int price = 10000;
        int salesStatus = 0;
        LocalDateTime registrationTime = LocalDateTime.now();

        long tradePostId = jdbcTemplateHelper.insertTestTradePost(sellerId, title, productStatus, category, description, price, salesStatus, registrationTime);

        MvcResult mvcResult = mockMvc.perform(post("/chat/room/tradePost")
                        .param("buyerId", String.valueOf(buyerId))
                        .param("tradePostId", String.valueOf(tradePostId))
                        .accept(MediaType.APPLICATION_JSON))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);

        String chatRoomId = (String) item.get("chatRoomId");
        ChatRoom chatRoom = chatRoomService.findById(chatRoomId);

        assertThat(chatRoom).isNotNull();
        assertThat(item.get("roomMakerId")).isEqualTo((int) sellerId);
        assertThat(item.get("guestId")).isEqualTo((int) buyerId);
    }

    @DisplayName("채팅방 정보를 요청하고 응답받은 body값을 체크")
    @Test
    void chatRoomInfoTest() throws Exception {

        String roomId = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();

        jdbcTemplateHelper.insertTestChatRoom(roomId, createdAt);

        MvcResult mvcResult = mockMvc.perform(get("/chat/room/")
                        .param("roomId", String.valueOf(roomId)))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("chatRoomId")).isEqualTo(roomId);
        assertThat(item.get("lastChatMesg")).isNull();
        assertThat(item.get("chatRoomMembers")).isEqualTo(Collections.EMPTY_LIST);
        assertThat(item.get("latestChatMessages")).isEqualTo(Collections.EMPTY_LIST);
    }

    @DisplayName("사용자별 채팅방 리스트를 요청하고 해당 채팅방의 정보가 잘 내려왔는지 체크")
    @Test
    void chatRoomListTest() throws Exception {

        String newEmail = "test@naver.com";
        String newNickname = "test";
        String newPassword = "password";
        LocalDateTime joinedAt = LocalDateTime.now();

        long roomMakerId = jdbcTemplateHelper.insertTestUser(newEmail, newNickname, newPassword, joinedAt);
        long guestId0 = jdbcTemplateHelper.insertTestUser(newEmail + 0, newNickname + 0, newPassword + 0, joinedAt.plusSeconds(1));
        long guestId1 = jdbcTemplateHelper.insertTestUser(newEmail + 1, newNickname + 1, newPassword + 1, joinedAt.plusSeconds(2));

        int roomSize = 2;
        String roomId = UUID.randomUUID().toString();
        LocalDateTime createdAt = LocalDateTime.now();

        jdbcTemplateHelper.insertTestChatRoom(roomId + "0", createdAt);
        jdbcTemplateHelper.insertTestChatRoom_Members(roomId + "0", roomMakerId, guestId0);

        jdbcTemplateHelper.insertTestChatRoom(roomId + "1", createdAt);
        jdbcTemplateHelper.insertTestChatRoom_Members(roomId + "1", roomMakerId, guestId1);

        MvcResult mvcResult = mockMvc.perform(get("/chat/room/list")
                        .param("userId", String.valueOf(roomMakerId)))

                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        assertThat(status).isEqualTo(200);
        assertThat(item.get("count")).isEqualTo(roomSize);

        JSONArray chatRooms = (JSONArray) item.get("chatRooms");

        // 첫번째 채팅방 정보
        LinkedHashMap chatRoom0 = (LinkedHashMap) chatRooms.get(0);
        assertThat(chatRoom0.get("chatRoomId")).isEqualTo(roomId + 0);

        JSONArray chatRoomMembers1 = (JSONArray) chatRoom0.get("chatRoomMembers");
        LinkedHashMap chatRoomMember0_1 = (LinkedHashMap) chatRoomMembers1.get(0);
        LinkedHashMap chatRoomMember0_2 = (LinkedHashMap) chatRoomMembers1.get(1);

        assertThat(chatRoomMember0_1.get("userId")).isIn(Arrays.asList((int) roomMakerId, (int) guestId0));
        assertThat(chatRoomMember0_2.get("userId")).isIn(Arrays.asList((int) roomMakerId, (int) guestId0));

        // 두번째 채팅방 정보
        LinkedHashMap chatRoom1 = (LinkedHashMap) chatRooms.get(1);
        assertThat(chatRoom1.get("chatRoomId")).isEqualTo(roomId + 1);

        JSONArray chatRoomMembers2 = (JSONArray) chatRoom1.get("chatRoomMembers");
        LinkedHashMap chatRoomMember1_1 = (LinkedHashMap) chatRoomMembers2.get(0);
        LinkedHashMap chatRoomMember1_2 = (LinkedHashMap) chatRoomMembers2.get(1);

        assertThat(chatRoomMember1_1.get("userId")).isIn(Arrays.asList((int) roomMakerId, (int) guestId1));
        assertThat(chatRoomMember1_2.get("userId")).isIn(Arrays.asList((int) roomMakerId, (int) guestId1));
    }
}
