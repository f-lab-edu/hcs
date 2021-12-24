package com.hcs.controller;

import com.hcs.config.EnableMockMvc;
import com.hcs.exception.ErrorCode;
import com.jayway.jsonpath.JsonPath;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableMockMvc
@EnableEncryptableProperties
@Transactional
public class ExceptionAdvisorTest {
    @Autowired
    MockMvc mockMvc;

    @DisplayName("Exception Handler - NumberFormatException - 숫자로 형식변환이 불가능한 문자열을 input으로 받음")
    @Test
    void BAD_REQUEST_NumberFormatException() throws Exception {
        String testStr = "wrong";

        MvcResult mvcResult = mockMvc.perform(get("/test/number/{id}", testStr))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        int status = JsonPath.parse(response).read("$.HCS.status");
        HashMap<String, Object> item = JsonPath.parse(response).read("$.HCS.item");

        ErrorCode error = ErrorCode.NUMBER_FORMAT;

        assertThat(status).isEqualTo(error.getStatus());
        assertThat(item.get("errorCode")).isEqualTo(error.getErrorCode());
        assertThat(item.get("message")).isEqualTo(error.getMessage());
    }
}
