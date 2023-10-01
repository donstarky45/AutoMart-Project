package com.automart.repository;

import com.automart.entity.Transactions;
import com.automart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    Transactions findByTransactionId(String id);



    List<Transactions> findAllByUserDetails(UserEntity userEntity);
}
