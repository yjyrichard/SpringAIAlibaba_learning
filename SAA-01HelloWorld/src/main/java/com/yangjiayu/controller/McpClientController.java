package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname McpClientController
 * @Description TODO
 * @Date 2025/9/27 22:16
 * @Created by YangJiaYu
 */
@RestController
public class McpClientController {

    @Autowired
    @Qualifier("mcp")
    private ChatClient chatClient;//使用mcp服务

    @GetMapping("/mcpclient/chat")
    public Flux<String> chat(@RequestParam(name = "msg",defaultValue = "北京") String msg){
        System.out.println("使用了mcp");
        return chatClient.prompt(msg).stream().content();
    }



}
