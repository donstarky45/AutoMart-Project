package com.automart.response;

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
public class CarAdsResponse {


    private String response;
    private String owner;
    private String carId;

    private String image;


    private Date createdOn;


    private String state;


    private String status;


    private double price;


    private String manufacturer;

    private String model;

    private String bodyType;

}
