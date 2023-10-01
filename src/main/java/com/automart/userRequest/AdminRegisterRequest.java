package com.automart.userRequest;

import com.automart.entity.AddressEntity;
import com.automart.entity.Role;
import com.automart.entity.Transactions;
import com.automart.entity.UserEntity;
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
