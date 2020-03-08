package com.study.sparkweb.sparkweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    //设置过期时间
    @Test
    public void testExpire(){
        redisTemplate.expire("foo",1, TimeUnit.MINUTES);
    }

    //获取过期时间
    @Test
    public void testGetExpire(){
        redisTemplate.getExpire("foo",TimeUnit.MINUTES);
    }

    //判断key是否存在
    @Test
    public void testHasKey(){

        boolean flage = redisTemplate.hasKey("foo");
        System.out.println(flage);
    }

    @Test
    public void getKeys() {
        Set<String> keys = redisTemplate.keys("*");
        for(String key : keys) {
            System.out.println(key);
        }
    }

    @Test
    public void testLPut() {
        redisTemplate.opsForList().leftPush("foo","zhangsan");
    }

    public void testA() {

    }
}
