package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname StreamOutputController
 * @Description 流失输出
 * @Date 2025/9/23 20:22
 * @Created by YangJiaYu
 */
@RestController
public class StreamOutputController {

    //V1 通过ChatModel实现stream实现流式输出
    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping(value = "/stream/chatflux1")
    public Flux<String> streamChatflux1(@RequestParam(name = "question",defaultValue = "你是谁？")String question){
        return deepseekChatModel.stream(question);
    }

    @GetMapping(value = "/stream/chatflux2")
    public Flux<String> streamChatflux2(@RequestParam(name = "question",defaultValue = "你是谁？")String question){
        return qwenChatModel.stream(question);
    }

    // V2 通过ChatClient实现stream实现流式输出
    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping(value = "/stream/chatflux3")
    public Flux<String>chatflux3(@RequestParam(name="question",defaultValue = "你是谁")String question){
        return deepseekChatClient.prompt(question).stream().content();
    }

    @GetMapping(value = "/stream/chatflux4")
    public Flux<String>chatflux4(@RequestParam(name="question",defaultValue = "你是谁")String question){
        return qwenChatClient.prompt(question).stream().content();
    }


}
