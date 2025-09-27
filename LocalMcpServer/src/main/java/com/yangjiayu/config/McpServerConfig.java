package com.yangjiayu.config;

import com.yangjiayu.Service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname McpServerConfig
 * @Description TODO
 * @Date 2025/9/27 21:57
 * @Created by YangJiaYu
 */
@Configuration
public class McpServerConfig {

    /**
     * 将工具方法暴露给外部mcp client调用
     * @param weatherService
     * @return
     */
    @Bean
    public ToolCallbackProvider weatherTools(WeatherService  weatherService) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(weatherService)
            .build();
    }
}
