package com.SecurityAug13th.RealTutorial.response;

import com.SecurityAug13th.RealTutorial.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageModel {
    private Integer id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String emailVerificationToken;
    private boolean emailVerificationStatus = false;
    private String accountNumber;
    private double balance;
    @Enumerated(EnumType.STRING)
    private Role role;

}
