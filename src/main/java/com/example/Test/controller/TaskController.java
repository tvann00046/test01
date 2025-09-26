package com.example.Test.controller;

import com.example.Test.dto.Task.PaginationResponseDto;
import com.example.Test.dto.Task.TaskRequestDto;
import com.example.Test.dto.Task.TaskResponseDto;
import com.example.Test.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public TaskResponseDto create(@Valid @RequestBody TaskRequestDto dto, Authentication authentication){
        String username = authentication.getName();
        return taskService.createTask(dto, username);
    }

    @GetMapping
    public PaginationResponseDto<TaskResponseDto> list(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Authentication authentication) {
        String username = authentication.getName();
        return taskService.getAllTasks(limit, offset, username);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getOne(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        return taskService.getTaskById(id, username);
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable String id, @Valid @RequestBody TaskRequestDto dto, Authentication authentication) {
        String username = authentication.getName();
        return taskService.updateTask(id, dto, username);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        taskService.deleteTask(id, username);
    }
}
