package com.SecurityAug13th.RealTutorial.repository;

import com.SecurityAug13th.RealTutorial.entity.AddressEntity;
import com.SecurityAug13th.RealTutorial.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    UserEntity findByAddressId(String id);
}
