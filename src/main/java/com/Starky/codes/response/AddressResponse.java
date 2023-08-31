package com.Starky.codes.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse extends EntityModel<AddressResponse> {
    private Long id;
    private String addressId;
    private String country;

    private String city;

    private String postalCode;

    private String billing;
}
