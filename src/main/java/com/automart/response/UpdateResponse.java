package com.automart.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateResponse extends EntityModel<UpdateResponse> {

private OperationalResult result;
    private String firstName;
    private String lastName;
}
