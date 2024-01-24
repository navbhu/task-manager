package com.example.gatling.dto;

import jakarta.validation.constraints.NotEmpty;

public record NewUser(@NotEmpty String email, @NotEmpty String emailReentered, @NotEmpty String password) {
}
