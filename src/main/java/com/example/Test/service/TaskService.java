package com.example.Test.service;

import com.example.Test.dto.*;
import com.example.Test.model.Task;
import com.example.Test.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = Task.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .completed(requestDto.isCompleted())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Task saved = taskRepository.save(task);
        return mapToResponse(saved);
    }

    public TaskResponseDto updateTask(String id, TaskRequestDto requestDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(requestDto.getTitle());
        task.setDescription(requestDto.getDescription());
        task.setCompleted(requestDto.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponseDto getTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return mapToResponse(task);
    }

    public PaginationResponseDto<TaskResponseDto> getAllTasks(int limit, int offset) {
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Task> page = taskRepository.findAll(pageable);

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

    private TaskResponseDto mapToResponse(Task task) {
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
