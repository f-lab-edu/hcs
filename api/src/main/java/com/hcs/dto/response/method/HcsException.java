package com.hcs.dto.response.method;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hcs.exception.result.ExceptionResult;
import com.hcs.exception.result.FieldErrorDetail;
import com.hcs.exception.result.ValidationResult;
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

    public ObjectNode validation(int status, ExceptionResult exceptionResult, ValidationResult errors) {
        ObjectNode hcs = this.exception(status, exceptionResult);

        ArrayNode errorLists = objectMapper.createArrayNode();

        for (FieldErrorDetail error : errors.getErrors()) {
            ObjectNode errorNode = objectMapper.valueToTree(error);
            errorLists.add(errorNode);
        }

        ObjectNode item = (ObjectNode) hcs.get("item");
        item.set("errors", errorLists);

        return hcs;
    }

    public ObjectNode exceptionAndLocation(int status, ExceptionResult exceptionResult, String location) {

        ObjectNode hcs = objectMapper.createObjectNode();
        ObjectNode item = objectMapper.valueToTree(exceptionResult);

        item.put("location", location);

        hcs.put("status", status);
        hcs.set("item", item);

        return hcs;
    }
}
