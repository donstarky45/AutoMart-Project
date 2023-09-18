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
public class UserUpdateDto implements Serializable {


    private static final Long serialVersionUID = 1L;

    private String firstName;
    private String lastName;
}
