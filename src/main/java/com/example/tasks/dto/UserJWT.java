package com.example.tasks.dto;

import java.time.Instant;

public record UserJWT(String sub, Instant exp) {
}
