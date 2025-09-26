package com.example.Test.repository;

import com.example.Test.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    Page<Task> findByUserId(String userId, Pageable pageable);
    Optional<Task> findByIdAndUserId(String id, String userId);
    long countByUserId(String userId);
}
