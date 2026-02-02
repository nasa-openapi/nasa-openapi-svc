package com.nasa.repository;

import com.nasa.entity.PushSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushSubscriptionRepository extends JpaRepository<PushSubscriptionEntity, Integer> {


}
