package com.example.Test.service;

import com.example.Test.dto.Task.PaginationResponseDto;
import com.example.Test.dto.Task.TaskRequestDto;
import com.example.Test.dto.Task.TaskResponseDto;
import com.example.Test.model.AppUser;
import com.example.Test.model.Task;
import com.example.Test.repository.TaskRepository;
import com.example.Test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // create by username (username from auth)
    public TaskResponseDto createTask(TaskRequestDto dto, String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = Task.builder()
                .userId(user.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .completed(dto.isCompleted())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    public TaskResponseDto updateTask(String id, TaskRequestDto dto, String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed to update this task");
        }
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());
        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    public void deleteTask(String id, String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed to delete this task");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponseDto getTaskById(String id, String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new RuntimeException("Task not found for this user"));
        return mapToResponse(task);
    }

    public PaginationResponseDto<TaskResponseDto> getAllTasks(int limit, int offset, String username){
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Task> page = taskRepository.findByUserId(user.getId(), pageable);

        return PaginationResponseDto.<TaskResponseDto>builder()
                .statusCode(200)
                .data(PaginationResponseDto.DataWrapper.<TaskResponseDto>builder()
                        .items(page.map(this::mapToResponse).getContent())
                        .meta(PaginationResponseDto.Meta.builder()
                                .limit(limit)
                                .offset(offset)
                                .total(page.getTotalElements())
                                .totalPages(page.getTotalPages())
                                .build())
                        .build())
                .build();
    }

    private TaskResponseDto mapToResponse(Task task){
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
