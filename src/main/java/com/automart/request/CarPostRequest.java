package com.automart.request;

import com.automart.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarPostRequest {




    private String carId;
    private UserEntity owner;
    private String image;
    private Date createdOn;
    private String state;
    private String status;
    private double price;
    private String manufacturer;
    private String model;
    private String bodyType;

}
