package com.automart.controllers;


import com.automart.response.DeleteResponse;
import com.automart.response.TransferResponse;
import com.automart.service.AdminService;
import com.automart.service.UserService;
import com.automart.userRequest.TransferRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Controller")  // swagger
public class AdminController {


    @Autowired
    private final UserService userService;
    private final AdminService adminService;
    @DeleteMapping(path = "/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Operation(
            description = "Delete endpoint for Admin",
            summary = "Admins Can Delete a user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Insufficient Funds/ User not Found",
                            responseCode = "200"
                    )
            }

    )
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }



}
