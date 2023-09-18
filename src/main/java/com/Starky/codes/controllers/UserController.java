package com.Starky.codes.controllers;




import com.Starky.codes.response.*;
import com.Starky.codes.service.UserService;
import com.Starky.codes.shared.UserUpdateDto;
import com.Starky.codes.userRequest.TransferRequest;
import com.Starky.codes.userRequest.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE })

    public UpdateResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userDetails) {
        UpdateResponse returnValue = new UpdateResponse();

        UserUpdateDto userDto = new UserUpdateDto();
        userDto = new ModelMapper().map(userDetails, UserUpdateDto.class);

        UserUpdateDto updateUser = userService.updateUser(id, userDto);
        returnValue = new ModelMapper().map(updateUser, UpdateResponse.class);

        return returnValue;
    }
    @PutMapping(path = "/{userId}/subscribe/{adminId}")
    public ResponseEntity<OperationalResult> subscribe( @PathVariable String userId,@PathVariable String adminId) {
        return ResponseEntity.ok(userService.subscribe(userId,adminId));
    }
    @DeleteMapping (path = "/{userId}/unsubscribe/{subscriptionId}")
    public ResponseEntity<OperationalResult> unSubscribe( @PathVariable String userId,@PathVariable String subscriptionId) {
        return ResponseEntity.ok(userService.unSubscribe(userId,subscriptionId));
    }
}