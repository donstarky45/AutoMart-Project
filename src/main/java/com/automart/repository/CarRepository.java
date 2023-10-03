package com.automart.repository;

import com.automart.entity.Car;
import com.automart.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByCarId(String carId);


    List<Car> findAllByStatus(String status);
    List<Car> findAllByBodyType(String status);

}
