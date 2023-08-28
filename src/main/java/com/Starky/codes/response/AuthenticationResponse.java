package com.Starky.codes.response;


import com.Starky.codes.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends EntityModel<AuthenticationResponse> {

    //    @JsonProperty("access_token")
//    private String accessToken;
//    @JsonProperty("refresh_token")
    private String token;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
    private List<AddressEntity> addresses;
    private Link link;


}