package com.automart.repository;


import com.automart.entity.SentOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<SentOrders, Long> {

    SentOrders findByOrderId(String orderId);

}