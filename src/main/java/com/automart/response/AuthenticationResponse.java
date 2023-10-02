package com.automart.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends EntityModel<AuthenticationResponse> {


    private String token;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;




}