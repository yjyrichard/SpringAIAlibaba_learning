package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

/**
 * @Classname ChatMemory4RedisController
 * @Description TODO
 * @Date 2025/9/25 18:35
 * @Created by YangJiaYu
 */
@RestController
public class ChatMemory4RedisController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/chatmemory/chat")
    public String chatMemory(String msg,String userId){
        if (userId == null || userId.trim().isEmpty()) {
            return "Error: userId parameter is required and cannot be empty";
        }

        return qwenChatClient.prompt(msg)
            .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID,userId))
            .call().content();

    }

}
