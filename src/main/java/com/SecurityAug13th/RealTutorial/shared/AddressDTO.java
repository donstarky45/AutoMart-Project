package com.SecurityAug13th.RealTutorial.shared;


import com.SecurityAug13th.RealTutorial.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO implements Serializable {

    private static final Long serialVersionUID = 1L;

    private long id;
    private String addressId;

    private String country;


    private String city;

    private String postalCode;

    private String billing;


    private UserDto userDetails;
}
