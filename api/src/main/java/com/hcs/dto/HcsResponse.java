package com.hcs.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HcsResponse {

    private ObjectNode HCS;

    public static HcsResponse HcsResponseUser(int status, User user, ObjectMapper objectMapper) {
        ObjectNode hcs = objectMapper.createObjectNode();

        ObjectNode item = objectMapper.createObjectNode();
        ObjectNode profile = objectMapper.valueToTree(user);

        item.put("userId", user.getId());
        item.set("profile", profile);

        hcs.put("status", status);
        hcs.set("item", item);

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .build();

        return response;
    }

    public static HcsResponse HcsResponseException(int status, ExceptionResult exceptionResult, ObjectMapper objectMapper) {

        ObjectNode hcs = objectMapper.createObjectNode();

        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .build();

        return response;
    }
}
