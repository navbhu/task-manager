package com.example.gatling.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateTask(@NotEmpty String name) {
}
