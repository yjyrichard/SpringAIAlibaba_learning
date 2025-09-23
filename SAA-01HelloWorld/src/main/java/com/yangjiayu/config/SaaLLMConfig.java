package com.yangjiayu.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname SaaLLMConfig
 * @Description 配置类
 * @Date 2025/9/23 17:54
 * @Created by YangJiaYu
 */
@Configuration
public class SaaLLMConfig {
    /**
     * 方式1：${}
     *
     */
    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public DashScopeApi dashScopeApi(){
        return DashScopeApi.builder().apiKey(apiKey).build();
    }

    /**
     * 方式2：System.getenv("环境变量")
     * 持有yml文件配置：spring.ai.dashscope.api-key=${}
     */
//    @Bean
//    public DashScopeApi dashScopeApi(){
//        return DashScopeApi.builder().apiKey(System.getenv("ALI_BAILIAN")).build();
//    }


}
