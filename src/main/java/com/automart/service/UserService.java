package com.automart.service;


import com.automart.response.*;
import com.automart.userRequest.RegisterRequest;
import com.automart.userRequest.TransferRequest;
import com.automart.userRequest.UserLoginRequest;
import com.automart.userRequest.UserUpdateRequest;

import java.util.List;

public interface UserService {

    AuthenticationResponse signup(RegisterRequest request);
  // AuthenticationResponse signupAdmin(AdminRegisterRequest request);
    AuthenticationResponse login(UserLoginRequest request);
    TransferResponse transfer(TransferRequest request, String userId);
    AuthenticationResponse getUser(String userId);
    DeleteResponse deleteUser(String userId);
    List<PageModel> getUsers(int page, int limit, org.springframework.data.domain.Pageable pageable);
    List<TransactionResponse> getTransactions(String userId);
    TransactionResponse getTransaction(String userId, String transactionId);
    List<AddressResponse> getAddresses(String userId);
    AddressResponse getAddress(String userId, String addressId);
    UpdateResponse updateUser(String userId, UserUpdateRequest user);

}
