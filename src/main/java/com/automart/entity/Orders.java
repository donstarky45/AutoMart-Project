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
public class Orders {
    @Id
    @GeneratedValue
    private long id;


    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userId;

    @Column(nullable = false)
    private String orderType;

    @Column(nullable = false)
   private String carId;

    @Column(nullable = false)
    private double amount;

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