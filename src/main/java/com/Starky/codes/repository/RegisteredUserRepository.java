package com.Starky.codes.repository;

import com.Starky.codes.entity.RegisteredUsers;
import com.Starky.codes.entity.Transactions;
import com.Starky.codes.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUsers, Long> {
    RegisteredUsers findByUserId(String id);

}
