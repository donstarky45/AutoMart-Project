package com.Starky.codes.controllers;



import com.Starky.codes.entity.UserEntity;
import com.Starky.codes.service.UserService;
import com.Starky.codes.response.AuthenticationResponse;
import com.Starky.codes.userRequest.RegisterRequest;
import com.Starky.codes.userRequest.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends EntityModel<UserEntity> {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public  ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


}
