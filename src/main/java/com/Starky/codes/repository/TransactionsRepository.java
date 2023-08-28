package com.Starky.codes.repository;

import com.Starky.codes.entity.Transactions;
import com.Starky.codes.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    UserEntity findByTransactionId(String id);
}
