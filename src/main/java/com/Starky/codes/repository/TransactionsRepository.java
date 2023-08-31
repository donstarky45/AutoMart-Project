package com.Starky.codes.repository;

import com.Starky.codes.entity.Transactions;
import com.Starky.codes.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    Transactions findByTransactionId(String id);



    List<Transactions> findAllByUserDetails(UserEntity userEntity);
}
