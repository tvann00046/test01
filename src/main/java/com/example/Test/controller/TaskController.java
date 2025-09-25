package com.example.Test.controller;

import com.example.Test.dto.*;
import com.example.Test.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public TaskResponseDto create(@Valid @RequestBody TaskRequestDto dto) {
        return taskService.createTask(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDto update(@PathVariable String id, @Valid @RequestBody TaskRequestDto dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    public TaskResponseDto getOne(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public PaginationResponseDto<TaskResponseDto> getAll(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return taskService.getAllTasks(limit, offset);
    }
}
