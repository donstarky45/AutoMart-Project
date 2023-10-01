package com.automart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Car")
public class Car implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String carId;

    @Column(unique = true, nullable = false)
    private String owner;

    @Column(unique = true, nullable = false)
    private Date createdOn;

    @Column(unique = true, nullable = false)
    private String state;

    @Column(unique = true, nullable = false)
    private String status;

    @Column(unique = true, nullable = false)
    private double price;

    @Column(unique = true, nullable = false)
    private String manufacturer;

    @Column(unique = true, nullable = false)
    private String model;
    @Column(unique = true, nullable = false)
    private String bodyType;



    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userId;




}


