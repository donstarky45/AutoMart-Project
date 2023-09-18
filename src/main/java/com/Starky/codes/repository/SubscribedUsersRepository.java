package com.Starky.codes.repository;

import com.Starky.codes.entity.RegisteredUsers;
import com.Starky.codes.entity.SubscribedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribedUsersRepository extends JpaRepository<SubscribedUsers, Long> {
    SubscribedUsers findByUserId(String id);
    SubscribedUsers findBySubscriptionId(String id);
}
