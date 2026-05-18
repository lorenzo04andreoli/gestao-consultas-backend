package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.LoginRequestDto;
import com.lorenzo.gestaoconsultas.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service){
        this.service = service;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody @Valid LoginRequestDto request) {
        String token = service.login(request);
        return Map.of("token", token);
    }
}
