package com.hcs.repository;

import com.hcs.domain.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Transactional(readOnly = true)
    Page<ChatMessage> findListsByRoomId(String roomId, Pageable pageable);
}
