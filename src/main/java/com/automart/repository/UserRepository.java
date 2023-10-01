package com.automart.repository;


import java.util.Optional;

import com.automart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity>  findByAccountNumber(String accountNumber);
   UserEntity  findByUserId(String userId);
}