package com.automart.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class SentOrders {
    @Id
    @GeneratedValue
    private long id;


    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userId;

    @Column(nullable = false)
    private String orderId;
    @Column(nullable = false)
    private String orderType;

    @Column(nullable = false)
   private String carId;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String  status;

}


//{
//        “id” : Integer,
//        “buyer” : Integer, // user id
//        “car_id” : Integer,
//        “amount” : Float, // price offered
//        “status” : String, // [pending, accepted, or rejected]
//        ...
//        }