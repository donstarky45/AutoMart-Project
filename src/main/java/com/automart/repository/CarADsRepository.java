package com.automart.repository;

import com.automart.entity.CarAD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarADsRepository extends JpaRepository<CarAD, Long> {


}
