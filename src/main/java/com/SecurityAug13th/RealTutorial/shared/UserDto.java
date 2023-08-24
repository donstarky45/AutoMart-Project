package com.SecurityAug13th.RealTutorial.shared;

import com.SecurityAug13th.RealTutorial.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {


    private static final Long serialVersionUID = 1L;

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String emailVerificationToken;
    private boolean emailVerificationStatus = false;
    private String userId;
    private String password;
    private String accountNumber;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Role role;
    private List<AddressDTO> addresses;
//    private List<TransactionDto> transactions;




}