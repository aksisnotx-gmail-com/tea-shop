package com.app.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis序列化
 * @author xxl
 * @since 2023/9/16
 */
@Configuration
public class RedisConfig {
    /**
     * 序列化相关的配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate  = new RedisTemplate<>();
        //支持事务,TODO 隐患在方法上加Transactional目的是控制mysql事务但是使用这个注解的方法使用了redis则也会进入事务，但是redis的事务中你先插入之后再查询插入的值是查不到的，因为没有落到库中
        //mysql是落到库中的
        //redisTemplate.setEnableTransactionSupport(true);
        //json方式序列化
        Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setConnectionFactory(factory);
        //String方式序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置所有的key为string方式序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //设置所有的value为jackson方式序列化
        redisTemplate.setValueSerializer(jackson);
        redisTemplate.setHashValueSerializer(jackson);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
