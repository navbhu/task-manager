package com.example.tasks.controller;

import com.example.tasks.dto.NewUser;
import com.example.tasks.dto.UserLogin;
import com.example.tasks.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody NewUser newUser) {
        String confirmationCode = authService.createUser(newUser);
        return ResponseEntity.ok(confirmationCode);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLogin userLogin) {
        String token = authService.login(userLogin);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@Valid @RequestParam String code) {
        authService.confirm(code);
        return ResponseEntity.ok().build();
    }


}
