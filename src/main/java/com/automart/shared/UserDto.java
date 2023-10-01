package com.automart.shared;

import com.automart.entity.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
    private String address;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Role role;





}