package com.yangjiayu.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname RedisMemoryConfig
 * @Description TODO
 * @Date 2025/9/25 13:47
 * @Created by YangJiaYu
 */
@Configuration
public class RedisMemoryConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository(){
        return RedisChatMemoryRepository.builder()
            .host(host)
            .password(password)
            .port(port)
            .build();
    }

}
