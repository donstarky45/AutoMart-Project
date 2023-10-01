package com.automart.controllers;





import com.automart.response.*;
import com.automart.service.UserService;
import com.automart.userRequest.TransferRequest;
import com.automart.userRequest.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @PutMapping(path = "/transfer/{userId}")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request, @PathVariable String userId) {
        return ResponseEntity.ok(userService.transfer(request, userId));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<AuthenticationResponse> getUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<PageModel>> getUsers(@RequestParam (value ="page", defaultValue = "0") int page,
                                                    @RequestParam(value = "limit", defaultValue = "25") int limit, Pageable pageable){
        return ResponseEntity.ok(userService.getUsers(page,limit, pageable));
    }

    @GetMapping(path = "/{userId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String userId){
        return ResponseEntity.ok(userService.getTransactions(userId));
    }
    @GetMapping(path = "/{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String userId, @PathVariable String transactionId ){
        return ResponseEntity.ok(userService.getTransaction(userId,transactionId));
    }

    @GetMapping(path = "/{userId}/addresses")
    public ResponseEntity<List<AddressResponse>> getAddresses(@PathVariable String userId){
        return ResponseEntity.ok(userService.getAddresses(userId));
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(@PathVariable String userId, @PathVariable String addressId ){
        return ResponseEntity.ok(userService.getAddress(userId,addressId));
    }

    @PutMapping(path = "/{id}")

    public ResponseEntity<UpdateResponse> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }


}