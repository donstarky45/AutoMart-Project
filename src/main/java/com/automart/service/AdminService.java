package com.automart.service;

import com.automart.response.AuthenticationResponse;
import com.automart.response.TransferResponse;
import com.automart.userRequest.AdminRegisterRequest;
import com.automart.userRequest.RegisterRequest;
import com.automart.userRequest.TransferRequest;
import com.automart.userRequest.UserLoginRequest;

public interface AdminService {


    AuthenticationResponse signupAdmin(AdminRegisterRequest request);
    AuthenticationResponse login(UserLoginRequest request);


}
