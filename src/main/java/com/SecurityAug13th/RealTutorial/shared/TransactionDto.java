package com.SecurityAug13th.RealTutorial.shared;

import com.SecurityAug13th.RealTutorial.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
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
