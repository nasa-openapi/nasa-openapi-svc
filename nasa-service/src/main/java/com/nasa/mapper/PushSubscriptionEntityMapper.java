package com.nasa.mapper;

import com.nasa.bean.PushSubscriptionBean;
import com.nasa.entity.PushSubscriptionEntity;

public class PushSubscriptionEntityMapper implements Mapper<PushSubscriptionBean, PushSubscriptionEntity>{
    public PushSubscriptionEntity map(PushSubscriptionBean dto) {
        return  PushSubscriptionEntity.builder()
                .endpoint(dto.getEndpoint())
                .publicKey(dto.getKeys().getP256dh())
                .authSecret(dto.getKeys().getAuth())
                .subscriber(dto.getName())
                .build();
    }
}
