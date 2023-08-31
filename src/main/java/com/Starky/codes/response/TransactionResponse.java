package com.Starky.codes.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse extends EntityModel<TransactionResponse> {

    private long id;
    private String transactionId;
    private String details;
    private String date;
}
