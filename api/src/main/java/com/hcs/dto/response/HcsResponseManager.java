package com.hcs.dto.response;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class HcsResponseManager {

    public HcsResponse makeHcsResponse(ObjectNode hcs) {

        HcsResponse response = HcsResponse.builder()
                .HCS(hcs)
                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        return response;
    }
}
