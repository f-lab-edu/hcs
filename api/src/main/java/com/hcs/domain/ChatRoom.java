package com.hcs.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "ChatRoom")
@DynamicUpdate
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastChatMesgId")
    private ChatMessage lastChatMesg;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ChatRoom_Members",
            joinColumns = @JoinColumn(name = "chatRoomId"),
            inverseJoinColumns = @JoinColumn(name = "memberId"))
    private Set<User> chatRoomMembers = new HashSet<>();

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public static ChatRoom create() {

        ChatRoom room = new ChatRoom();
        room.setId(UUID.randomUUID().toString());

        return room;
    }

    public void addMembers(User roomMaker, User guest) {
        this.chatRoomMembers.add(roomMaker);
        this.chatRoomMembers.add(guest);
    }
}