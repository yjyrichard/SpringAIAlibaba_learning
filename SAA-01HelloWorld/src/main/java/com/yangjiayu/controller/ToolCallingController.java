package com.yangjiayu.controller;

import com.yangjiayu.tools.DateTimeTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname ToolCallingController
 * @Description
 * @Date 2025/9/27 20:49
 * @Created by YangJiaYu
 */
@RestController
public class ToolCallingController {

    private final ChatClient chatClient;

    private final ChatModel  chatModel;

    public ToolCallingController(@Qualifier("deepseekChatClient") ChatClient chatClient,@Qualifier("deepseek") ChatModel chatModel) {
        this.chatClient = chatClient;

        this.chatModel = chatModel;
    }

    @GetMapping("/toolcall/chat")
    public String chat(@RequestParam(name = "msg",defaultValue = "你是谁？现在几点")String msg){

        //1.工具注册到工具集合中
        ToolCallback[] tools = ToolCallbacks.from(new DateTimeTools());

        //2.将工具集配置进ChatOptions对象
        ChatOptions options = ToolCallingChatOptions.builder().toolCallbacks(tools).build();

        //3.构建提示词
        Prompt prompt = new Prompt(msg, options);

        //4.调用大模型
        return chatModel.call(prompt).getResult().getOutput().getText();
    }


    @GetMapping("/toolcall/chat2")
    public Flux<String> chat2(@RequestParam(name = "msg",defaultValue = "你是谁？现在几点")String msg){

        return chatClient.prompt(msg)
            .stream()
            .content();
    }
    //DeepSeek 目前的 流式接口不会下发 function_call，所以
    //chatClient.prompt(msg).stream().content()
    //永远触发不到工具；
    //改用
    //chatClient.prompt(msg).call().content()
    //就能立刻看到时间。

}
