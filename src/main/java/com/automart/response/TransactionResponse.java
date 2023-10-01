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
public class TransactionResponse extends EntityModel<TransactionResponse> {

    private long id;
    private String transactionId;
    private String details;
    private String date;
}
