package com.automart.service;


import com.automart.response.*;
import com.automart.request.CarPostRequest;
import com.automart.request.RegisterRequest;
import com.automart.request.UserLoginRequest;
import com.automart.request.UserUpdateRequest;

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
}
