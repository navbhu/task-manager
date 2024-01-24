package com.example.tasks.dto;

import jakarta.validation.constraints.NotEmpty;

public record NewUser(@NotEmpty String email, @NotEmpty String emailReentered, @NotEmpty String password) {
}
