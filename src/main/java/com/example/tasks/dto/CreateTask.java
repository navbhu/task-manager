package com.example.tasks.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateTask(@NotEmpty String name) {
}
