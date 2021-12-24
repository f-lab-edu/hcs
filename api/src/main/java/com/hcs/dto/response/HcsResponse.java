package com.hcs.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"HCS", "createdAt"})
public class HcsResponse {

    @JsonProperty("HCS")
    private ObjectNode HCS;
    @JsonProperty("createdAt")
    private String createdAt;
}
