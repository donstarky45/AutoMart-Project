package com.Starky.codes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RegisteredUsers")
public class RegisteredUsers implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String firstName;

    @Column(unique = true, nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "adminId")
    private UserEntity userDetails;
}
