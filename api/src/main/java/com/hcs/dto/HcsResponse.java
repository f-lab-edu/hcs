package com.hcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"HCS", "createdAt"})
public class HcsResponse {

    @JsonProperty("HCS")
    private ObjectNode HCS;

    @JsonProperty("createdAt")
    private String createdAt;

    private static HcsResponse HcsResponseUser(int status, User user, ObjectMapper objectMapper) {
        ObjectNode hcs = objectMapper.createObjectNode();

        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode profile = objectMapper.valueToTree(user);

        item.put("userId", user.getId());
        item.set("profile", profile);

        hcs.put("status", status);
        hcs.set("item", item);

        return makeHcsResponse(hcs);
    }

    private static HcsResponse HcsResponseException(int status, ExceptionResult exceptionResult, ObjectMapper objectMapper) {

        ObjectNode hcs = objectMapper.createObjectNode();

        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        return makeHcsResponse(hcs);
    }

    private static HcsResponse makeHcsResponse(ObjectNode hcs) {

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return response;
    }
}
