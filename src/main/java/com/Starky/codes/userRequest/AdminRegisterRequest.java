package com.Starky.codes.userRequest;

import com.Starky.codes.entity.AddressEntity;
import com.Starky.codes.entity.Role;
import com.Starky.codes.entity.Transactions;
import com.Starky.codes.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accountNumber;
    private Role role = Role.ADMIN;
    private List<AddressEntity> addresses;
    private List<Transactions> transactions;
    private List<UserEntity> userDetails;
}
