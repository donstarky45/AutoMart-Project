package com.Starky.codes.controllers;



import com.Starky.codes.entity.UserEntity;
import com.Starky.codes.service.AdminService;
import com.Starky.codes.service.UserService;
import com.Starky.codes.response.AuthenticationResponse;
import com.Starky.codes.userRequest.AdminRegisterRequest;
import com.Starky.codes.userRequest.RegisterRequest;
import com.Starky.codes.userRequest.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping("/admin/signup/{adminId}")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request,@PathVariable String adminId) {
        return ResponseEntity.ok(adminService.signupUser(request,adminId));
    }

    }
