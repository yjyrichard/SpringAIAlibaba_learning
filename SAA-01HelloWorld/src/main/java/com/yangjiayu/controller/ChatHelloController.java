package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname ChatHelloController
 * @Description TODO
 * @Date 2025/9/23 17:59
 * @Created by YangJiaYu
 */
@RestController
public class ChatHelloController {

    // 不要导错 这个导入springai
    // @Resouce(name="ollamaChatModel") 使用@Qualifier
    @Resource // 对话模型，调用阿里云百炼平台
    private ChatModel chatModel;

//    // 不支持自动注入，只能手动注入
//    @Resource
//    private ChatClient chatClient;



    /**
     * 通用调用
     * @param msg
     * @return
     */
    @GetMapping(value = "/hello/dochat")
    public String doChat(@RequestParam(name="msg",defaultValue = "你是谁")String msg){
        String result = chatModel.call(msg);
        return result;
    }

    /**
     * 流式返回调用
     * @param msg
     * @return
     */
    @GetMapping(value = "/hello/streamchat")
    public Flux<String> stream(@RequestParam(name="msg",defaultValue = "你是谁")String msg){
        Flux<String> result = chatModel.stream(msg);
        return result;
    }



}
