package com.example.gatling.dto;

import java.time.Instant;
import java.util.UUID;

public record TaskDTO(UUID id, String name, TaskStatus status, Instant created) {
}
