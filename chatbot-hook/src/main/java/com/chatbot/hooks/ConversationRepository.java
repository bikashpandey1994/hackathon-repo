package com.chatbot.hooks;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chatbot.hooks.models.ConversationEntity;

@Repository
public interface ConversationRepository extends CrudRepository<ConversationEntity, String> {

}
