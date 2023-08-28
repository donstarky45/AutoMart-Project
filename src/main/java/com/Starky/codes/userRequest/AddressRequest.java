package com.Starky.codes.userRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {


    private String country;

    private String city;

    private String postalCode;

    private String billing;
}
