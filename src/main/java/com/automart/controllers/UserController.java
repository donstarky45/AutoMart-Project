package com.automart.controllers;





import com.automart.entity.Car;
import com.automart.request.CarFilterRequest;
import com.automart.request.OrderRequest;
import com.automart.response.*;
import com.automart.service.UserService;
import com.automart.request.CarPostRequest;
import com.automart.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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



    @PutMapping(path = "/{id}")
    public ResponseEntity<UpdateResponse> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }


    @PostMapping(path = "/ads/{id}")
    public ResponseEntity<CarAdsResponse> postAd(@RequestBody CarPostRequest request, @PathVariable String id) {
        return ResponseEntity.ok(userService.postAd(request,id));
    }

    @PostMapping(path = "/{userId}/{carId}")
    public ResponseEntity<OrderResponse> orderPurchase(@RequestBody OrderRequest request, @PathVariable String userId,@PathVariable String carId) {
        return ResponseEntity.ok(userService.orderPurchase(request, userId, carId));
    }

    @PutMapping(path = "/{userId}/{orderId}")
    public ResponseEntity<ResponseMessages> updatePrice(@RequestBody OrderRequest request, @PathVariable String userId, @PathVariable String orderId) {
        return ResponseEntity.ok(userService.updatePrice(request, userId, orderId));
    }

    @PutMapping(path = "/{userId}/{orderId}/accept")
    public ResponseEntity<ResponseMessages> acceptOrder( @PathVariable String userId, @PathVariable String orderId) {
        return ResponseEntity.ok(userService.acceptOrder( userId, orderId));
    }

    @PutMapping(path = "/{userId}/{orderId}/reject")
    public ResponseEntity<ResponseMessages> rejectOrder( @PathVariable String userId, @PathVariable String orderId) {
        return ResponseEntity.ok(userService.rejectOrder(userId, orderId));
    }


    @PutMapping(path = "/{carId}/sold")
    public ResponseEntity<OperationalResult> markAsSold( @PathVariable String carId) {
        return ResponseEntity.ok(userService.markAsSold( carId));
    }

    @PutMapping(path = "/{carId}/price")
    public ResponseEntity<OperationalResult> adPriceUpdate( @RequestBody OrderRequest request, @PathVariable String carId) {
        return ResponseEntity.ok(userService.adPriceUpdate(request, carId));
    }

    @GetMapping(path = "view/{carId}")
    public ResponseEntity<CarAdsResponse> viewCar(@PathVariable String carId) {
        return ResponseEntity.ok(userService.viewCar(carId));
    }

    @GetMapping(path = "/cars/available")
    public ResponseEntity<List<CarAdsResponse>> viewAvailaleCars() {
        return ResponseEntity.ok(userService.viewAvailaleCars());
    }

    @GetMapping(path = "/cars/price/filter")
    public ResponseEntity<List<CarAdsResponse>> viewAvailaleCarsWithPrice(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(userService.viewAvailaleCarsWithPrice(request));
    }
    @GetMapping(path = "/cars/type")
    public ResponseEntity<List<CarAdsResponse>> viewCarsByBodyType(@RequestBody CarFilterRequest request) {
        return ResponseEntity.ok(userService.viewCarsByBodyType(request));
    }

    @GetMapping(path = "/cars/new")
    public ResponseEntity<List<CarAdsResponse>> viewAvailableAndNewCars() {
        return ResponseEntity.ok(userService.viewAvailableAndNewCars());
    }

    @GetMapping(path = "/cars/used")
    public ResponseEntity<List<CarAdsResponse>> viewAvailableAndUsedCars() {
        return ResponseEntity.ok(userService.viewAvailableAndUsedCars());
    }

    @GetMapping(path = "/cars/manufacturer")
    public ResponseEntity<List<CarAdsResponse>> viewAvailableCarsManufacturer(@RequestBody CarFilterRequest request) {
        return ResponseEntity.ok(userService.viewAvailableCarsManufacturer(request));
    }

}