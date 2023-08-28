package com.Starky.codes.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteResponse {
    private String operationalResult;
    private String operationalName;
}
