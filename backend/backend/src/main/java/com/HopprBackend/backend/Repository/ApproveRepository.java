package com.HopprBackend.backend.Repository;

import com.HopprBackend.backend.Entity.ApproveUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApproveRepository extends MongoRepository<ApproveUser, String> {
}
