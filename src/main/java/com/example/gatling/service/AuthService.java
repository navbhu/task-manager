package com.example.gatling.service;

import com.example.gatling.dto.NewUser;
import com.example.gatling.dto.UserJWT;
import com.example.gatling.dto.UserLogin;
import com.example.gatling.model.User;
import com.example.gatling.repository.UserRepository;
import com.example.gatling.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;


    public String createUser(NewUser newUser) {
        if(newUser.email().equals(newUser.emailReentered())) {
            String passwordHash = DigestUtils.md5DigestAsHex(newUser.password().getBytes(StandardCharsets.UTF_8));
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setEmail(newUser.email());
            user.setPassword(passwordHash);
            user.setValidationStatus("CREATED");
            user.setCreated(Instant.now());
            user = userRepository.save(user);
            return jwtUtil.createToken(user);
        }
        return "";
    }

    public String login(UserLogin userLogin) {
        User user = userRepository.findByEmail(userLogin.email()).orElseThrow();
        String passwordHash = DigestUtils.md5DigestAsHex(userLogin.password().getBytes(StandardCharsets.UTF_8));
        if(user.getPassword().equals(passwordHash) && "VALIDATED".equals(user.getValidationStatus())) {
            return jwtUtil.createToken(user);
        }
        return "";
    }

    public void confirm(String code) {
        try {
            JWT jwt = JWTParser.parse(code);
            String payload = jwt.getParsedString().split("\\.")[1];
            UserJWT userJWT = objectMapper.readValue(Base64.decodeBase64(payload), UserJWT.class);
            if(Instant.now().isBefore(userJWT.exp())) {
                User user = userRepository.findByEmail(userJWT.sub()).orElseThrow();
                user.setValidationStatus("VALIDATED");
                user = userRepository.save(user);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
