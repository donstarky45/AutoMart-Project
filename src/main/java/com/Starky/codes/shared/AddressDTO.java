package com.Starky.codes.shared;


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
