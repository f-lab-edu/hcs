package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.config.advisor.result.ExceptionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HcsException {

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectNode exception(int status, ExceptionResult exceptionResult) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        hcs.put("status", status);
        hcs.set("item", item);

        return hcs;
    }
}
