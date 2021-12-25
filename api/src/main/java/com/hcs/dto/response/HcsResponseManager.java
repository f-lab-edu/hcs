package com.hcs.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import com.hcs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class HcsResponseManager {

    @Autowired
    public Info info;

    @Autowired
    private ObjectMapper objectMapper;

    private HcsResponse makeHcsResponse(ObjectNode hcs) {

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return response;
    }

    public HcsResponse Exception(int status, ExceptionResult exceptionResult) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        return makeHcsResponse(hcs);
    }

    @Component
    public class Info {
        private ObjectNode profile(User user) {
            ObjectNode item = objectMapper.createObjectNode();
            ObjectNode profile = objectMapper.valueToTree(user);

            item.put("userId", user.getId());

            item.set("profile", profile);

            return item;
        }

        public HcsResponse User(User user) {
            ObjectNode hcs = objectMapper.createObjectNode();
            ObjectNode item = profile(user);

            hcs.put("status", 200);
            hcs.set("item", item);

            return makeHcsResponse(hcs);
        }

        // TODO 다른 도메인들은 함수를 태워 분기처리하여 item 필드 값을 채움
    }
}
