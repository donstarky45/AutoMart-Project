package com.SecurityAug13th.RealTutorial.repository;


import java.util.Optional;

import com.SecurityAug13th.RealTutorial.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity>  findByAccountNumber(String accountNumber);
   Optional<UserEntity>  findByUserId(String userId);
}