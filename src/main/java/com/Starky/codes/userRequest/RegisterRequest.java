package com.Starky.codes.userRequest;


import com.Starky.codes.entity.Role;
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