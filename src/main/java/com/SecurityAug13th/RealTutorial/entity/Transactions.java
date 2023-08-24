package com.SecurityAug13th.RealTutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TransactionMain")
public class Transactions implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 15)
    private String transactionId;

    @Column(nullable = false, length = 200)
    private String details;

    @Column(nullable = false, length = 100)
    private String date;



    @ManyToOne
    @JoinColumn(name = "TutorialNew_id")
    private UserEntity userDetails;
}
