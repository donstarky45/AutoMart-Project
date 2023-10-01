package com.automart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Addresses")
public class AddressEntity implements Serializable {


    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 15)
    private String addressId;

    @Column(nullable = false, length = 15)
    private String country;

    @Column(nullable = false, length = 15)
    private String city;

    @Column(nullable = false, length = 6)
    private String postalCode;

    @Column(nullable = false, length = 10)
    private String billing;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}
