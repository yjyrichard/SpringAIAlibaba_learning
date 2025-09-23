package com.yangjiayu.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // 模型名称常量定义 一套系统多模型共存
    private final String DEEPSEEK_MODEL = "deepseek-v3";

    private final String QWEN_MODEL = "qwen-max";


    @Bean(name = "deepseek")
    public ChatModel deepSeek(){
        return DashScopeChatModel.builder()
            .dashScopeApi(DashScopeApi.builder().apiKey(System.getenv("ALI_BAILIAN")).build())
            .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
            .build();
    }

    @Bean(name = "qwen")
    public ChatModel qwen(){
        return DashScopeChatModel.builder()
            .dashScopeApi(DashScopeApi.builder().apiKey(System.getenv("ALI_BAILIAN")).build())
            .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
            .build();
    }

    @Bean(name = "deepseekChatClient")
    public ChatClient deepSeekChatClient(@Qualifier("deepseek")ChatModel deepSeek){
        return ChatClient.builder(deepSeek).defaultOptions(ChatOptions.builder().model(DEEPSEEK_MODEL).build())
        .build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen")ChatModel qwen){
        return ChatClient.builder(qwen).defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
            .build();
    }


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

    @Bean
    public ChatClient chatClient(@Qualifier("dashscopeChatModel") ChatModel dashScopeChatModel){
        return ChatClient.builder(dashScopeChatModel).build();
    }




}
