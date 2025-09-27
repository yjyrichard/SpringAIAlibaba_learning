package com.yangjiayu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Classname RedisConfig
 * @Description Redis配置类
 * @Date 2025/9/27 16:20
 * @Created by YangJiaYu
 */
@Configuration
@Slf4j
public class RedisConfig {
    /**
     * RedisTemplate配置
     * redis序列化的工具配置类，下面请一定要开启配置
     * 127.0.0.1：6379> key *
     * 1)"ord:102" 序列化过
     * 2)"\xac\xed\x00xxxaord:102" 没有序列化过
     * this.redisTemplate.opsForValue();//提供了操作string类型的所有方法
     * this.redisTemplate.opsForList();//提供了操作list类型的所有方法、
     * this.redisTemplate.opsForSet();//提供了操作set的所有方法
     * this.redisTemplate.opsForHash();//提供了操作hash表的所有方法
     * this.redisTemplate.opsForZSet();//提供了操作zset的所有方法
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key序列化方式string
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化方式json,使用GenericJackson2JsonRedisSerializer替换默认序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
