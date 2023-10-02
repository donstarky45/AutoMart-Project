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
@Table(name = "Cars")
public class Car implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true, nullable = false)
    private String carId;

    @ManyToOne
    @JoinColumn(name = "owner")
    private UserEntity owner;


    @Column(nullable = false)
    private String image;

    @Column( nullable = false)
    private Date createdOn;

    @Column( nullable = false)
    private String state;

    @Column( nullable = false)
    private String status;

    @Column( nullable = false)
    private double price;

    @Column( nullable = false)
    private String manufacturer;

    @Column( nullable = false)
    private String model;

    @Column(nullable = false)
    private String bodyType;







}


