package com.example.Test.dto.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDto {
    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    private String description;

    private boolean completed = false;
}
