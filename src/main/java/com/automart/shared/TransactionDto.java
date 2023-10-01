package com.automart.shared;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto implements Serializable {

    private static final Long serialVersionUID = 1L;

    private long id;


    private String transactionId;


    private String details;


    private String date;


    private UserDto userDetails;

}
