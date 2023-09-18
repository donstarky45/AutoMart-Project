package com.Starky.codes.service;

import com.Starky.codes.response.*;
import com.Starky.codes.shared.UserUpdateDto;
import com.Starky.codes.userRequest.AdminRegisterRequest;
import com.Starky.codes.userRequest.RegisterRequest;
import com.Starky.codes.userRequest.TransferRequest;
import com.Starky.codes.userRequest.UserLoginRequest;

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
    UserUpdateDto updateUser(String userId, UserUpdateDto user);
  OperationalResult subscribe(String userId, String adminId);

  OperationalResult unSubscribe(String userId, String adminId);
}
