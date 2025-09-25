package com.example.Test.repository;

import com.example.Test.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepository extends MongoRepository<Task, String> {
    Page<Task> findByCompleted(boolean completed, Pageable pageable);
}
