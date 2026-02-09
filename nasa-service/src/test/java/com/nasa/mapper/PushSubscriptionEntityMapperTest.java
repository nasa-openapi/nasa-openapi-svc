package com.nasa.mapper;

import com.nasa.bean.PushSubscriptionBean;
import com.nasa.entity.PushSubscriptionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PushSubscriptionEntityMapperTest {

    @Test
    public void test_successfull_map_operation(){
        PushSubscriptionEntityMapper mapper = new PushSubscriptionEntityMapper();
        PushSubscriptionBean bean = PushSubscriptionBean.builder()
                .endpoint("testing")
                .keys(PushSubscriptionBean.Keys.builder()
                        .p256dh("key_value")
                        .auth("auth_value")
                        .build())
                .name("test")
                .build();
        PushSubscriptionEntity result =mapper.map(bean);
        Assertions.assertEquals("testing",result.getEndpoint());
        Assertions.assertEquals("test",result.getSubscriber());
        Assertions.assertEquals("key_value",result.getPublicKey());
        Assertions.assertEquals("auth_value",result.getAuthSecret());

    }
}
