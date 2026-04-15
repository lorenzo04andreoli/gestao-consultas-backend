package com.lorenzo.gestaoconsultas.controller;

import com.lorenzo.gestaoconsultas.dto.LoginRequestDto;
import com.lorenzo.gestaoconsultas.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service){
        this.service = service;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto request) {
        return service.login(request);
    }
}
