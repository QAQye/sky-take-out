package com.sky.service.impl;


import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopMapper shopMapper;
//    redis已经完成直接注入即可
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void setShopStatus(Integer status) {
        ValueOperations valueOperations =redisTemplate.opsForValue();
        valueOperations.set("SHOP_STATUS",status);

    }

    @Override
    public Integer showShopStatus() {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        return (Integer) valueOperations.get("SHOP_STATUS");
    }
}
