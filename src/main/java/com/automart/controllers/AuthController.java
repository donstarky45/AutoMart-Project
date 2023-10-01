package com.automart.controllers;



import com.automart.entity.UserEntity;
import com.automart.service.AdminService;
import com.automart.service.UserService;
import com.automart.response.AuthenticationResponse;
import com.automart.userRequest.AdminRegisterRequest;
import com.automart.userRequest.RegisterRequest;
import com.automart.userRequest.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends EntityModel<UserEntity> {
@Autowired
    private final UserService userService;
@Autowired
    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    @PostMapping("/admin/login")
    public  ResponseEntity<AuthenticationResponse> adminLogin(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(adminService.login(request));
    }
    @PostMapping("/login")
    public  ResponseEntity<AuthenticationResponse> authenticate(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    @PostMapping("/admin/signup")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(adminService.signupAdmin(request));
    }


    }
