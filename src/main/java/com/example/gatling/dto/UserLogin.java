package com.example.gatling.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserLogin(@NotEmpty String email, @NotEmpty String password) {
}
