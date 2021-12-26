package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @EqualsAndHashCode.Include
    private String id;

    private Set<ChatMessage> chatMessages = new HashSet<>();
    private Set<User> members = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
