package com.Starky.codes.repository;

import com.Starky.codes.entity.AddressEntity;
import com.Starky.codes.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    UserEntity findByAddressId(String id);
}
