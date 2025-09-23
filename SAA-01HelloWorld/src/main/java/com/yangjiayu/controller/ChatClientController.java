package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ChatClientController
 * @Description http://java2ai.com/docs/1.0.0.2/tutorials/basics/chat-client/?spm=5176.29160081.0.0.30a7aa5cJQIqRb
 * @Date 2025/9/23 18:54
 * @Created by YangJiaYu
 */
@RestController
public class ChatClientController {

    private final ChatClient dashScopeChatClient;

    /**
     * ChatClient不支持自动输入
     * @param dashScopeChatModel
     */
    public ChatClientController(ChatModel dashScopeChatModel) {
        this.dashScopeChatClient = ChatClient.builder(dashScopeChatModel).build();

    }

    @GetMapping("/chatclient/dochat")
    public String doChat(@RequestParam(name="msg",defaultValue = "2加9等于几")String msg){
        return dashScopeChatClient.prompt().user(msg).call().content();
    }
}
