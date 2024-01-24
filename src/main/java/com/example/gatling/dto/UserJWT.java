package com.example.gatling.dto;

import java.time.Instant;

public record UserJWT(String sub, Instant exp) {
}
