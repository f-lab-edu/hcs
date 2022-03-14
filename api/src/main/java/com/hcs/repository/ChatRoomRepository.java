package com.hcs.repository;

import com.hcs.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @Transactional(readOnly = true)
    Page<ChatRoom> findListsByChatRoomMembersId(long userId, Pageable pageable);
}
