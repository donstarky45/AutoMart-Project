package com.SecurityAug13th.RealTutorial.response;


import com.SecurityAug13th.RealTutorial.entity.AddressEntity;
import com.SecurityAug13th.RealTutorial.userRequest.AddressRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

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


}