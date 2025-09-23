package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;

/**
 * @Classname ChatClientControllerV2
 * @Description TODO
 * @Date 2025/9/23 19:50
 * @Created by YangJiaYu
 */
public class ChatClientControllerV2 {

    @Resource
    private ChatModel chatModel;

    @Resource
    private ChatClient dashScopeChatClientV2;

    // 同时存在 混合使用

}
