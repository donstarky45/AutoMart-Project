package com.automart.service;


import com.automart.request.*;
import com.automart.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

    AuthenticationResponse signup(RegisterRequest request);
  // AuthenticationResponse signupAdmin(AdminRegisterRequest request);
    AuthenticationResponse login(UserLoginRequest request);

    AuthenticationResponse getUser(String userId);
    DeleteResponse deleteUser(String userId);
    List<PageModel> getUsers(int page, int limit, org.springframework.data.domain.Pageable pageable);
    List<TransactionResponse> getTransactions(String userId);
    TransactionResponse getTransaction(String userId, String transactionId);

    UpdateResponse updateUser(String userId, UserUpdateRequest user);
  CarAdsResponse postAd(CarPostRequest request, String id);
  OrderResponse orderPurchase( OrderRequest request, String userId, String carId);

  ResponseMessages updatePrice( OrderRequest request,  String userId,  String orderId);
  public ResponseMessages acceptOrder( String userId,  String orderId);

  public ResponseMessages rejectOrder( String userId,  String orderId);
}
