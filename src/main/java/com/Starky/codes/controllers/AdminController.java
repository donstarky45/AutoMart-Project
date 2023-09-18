package com.Starky.codes.controllers;


import com.Starky.codes.response.DeleteResponse;
import com.Starky.codes.response.TransferResponse;
import com.Starky.codes.service.AdminService;
import com.Starky.codes.service.UserService;
import com.Starky.codes.userRequest.TransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {


    @Autowired
    private final UserService userService;
    private final AdminService adminService;
    @DeleteMapping(path = "/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }


    @PutMapping(path = "/fund/{adminId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request, @PathVariable String adminId) {
        return ResponseEntity.ok(adminService.fundUser(request, adminId));
    }
}
