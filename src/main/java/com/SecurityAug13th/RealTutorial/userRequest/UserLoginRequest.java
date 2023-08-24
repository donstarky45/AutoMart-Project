package com.SecurityAug13th.RealTutorial.userRequest;





import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    private String email;
    String password;
    private List<AddressRequest> addressRequests;


}