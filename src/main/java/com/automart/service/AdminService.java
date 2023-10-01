package com.automart.service;

import com.automart.response.AuthenticationResponse;
import com.automart.request.AdminRegisterRequest;
import com.automart.request.UserLoginRequest;

public interface AdminService {


    AuthenticationResponse signupAdmin(AdminRegisterRequest request);
    AuthenticationResponse login(UserLoginRequest request);


}
