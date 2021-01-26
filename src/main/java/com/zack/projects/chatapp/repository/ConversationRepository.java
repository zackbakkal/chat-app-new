package com.zack.projects.chatapp.repository;

import com.zack.projects.chatapp.entity.UserConversation;
import com.zack.projects.chatapp.entity.ConversationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<UserConversation, ConversationId> {
}
