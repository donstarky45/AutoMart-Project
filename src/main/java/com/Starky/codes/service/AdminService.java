package com.Starky.codes.service;

import com.Starky.codes.response.AuthenticationResponse;
import com.Starky.codes.response.TransferResponse;
import com.Starky.codes.userRequest.AdminRegisterRequest;
import com.Starky.codes.userRequest.RegisterRequest;
import com.Starky.codes.userRequest.TransferRequest;
import com.Starky.codes.userRequest.UserLoginRequest;

public interface AdminService {


    AuthenticationResponse signupAdmin(AdminRegisterRequest request);
    AuthenticationResponse login(UserLoginRequest request);
  AuthenticationResponse signupUser(RegisterRequest request, String userId);
    TransferResponse fundUser(TransferRequest request, String adminId);
}
