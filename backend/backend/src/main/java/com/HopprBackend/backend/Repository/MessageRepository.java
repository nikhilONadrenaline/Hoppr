package com.HopprBackend.backend.Repository;

import com.HopprBackend.backend.Entity.Message;
import com.HopprBackend.backend.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}
