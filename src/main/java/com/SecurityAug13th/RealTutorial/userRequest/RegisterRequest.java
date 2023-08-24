package com.SecurityAug13th.RealTutorial.userRequest;


import com.SecurityAug13th.RealTutorial.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountNumber;
    private Role role = Role.USER;
    private List<AddressRequest> addresses;
//    private List<TransactionRequest> transactions;
}