package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Classname PromptController
 * @Description TODO
 * @Date 2025/9/23 21:23
 * @Created by YangJiaYu
 */
@RestController
public class PromptController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;


    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/prompt/lawchat")
    public Flux<String> chat(String question){
        return deepseekChatClient.prompt()
            //AI能力边界
            .system("你是一个法律助手，只回答法律问题，其它问题回复：我只能回答法律相关问题，其他无可奉告。")
            .user(question)
            .stream()
            .content();
    }

    @GetMapping("/prompt/chat2")
    public Flux<ChatResponse> chat2(String question){

        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手，每个故事控制在100字以内。");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage); // 注意顺序：系统消息在前

        return deepseekChatModel.stream(prompt);

    }


    @GetMapping("/prompt/chat3")
    public Flux<String> chat3(String question){

        // 系统消息
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手，每个故事控制在100字以内。");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage); // 注意顺序：系统消息在前


        return deepseekChatModel.stream(prompt)
            .map(response->response.getResults().get(0).getOutput().getText());

    }

    @GetMapping("/prompt/chat4")
    public String chat4(String question){
        AssistantMessage assistantMessage = deepseekChatClient.prompt()
            .user(question)
            .call()
            .chatResponse()
            .getResult()
            .getOutput();


        return assistantMessage.getText();
    }

    /**
     * tool  模型都是预训练
     */
    @GetMapping("/prompt/chat5")
    public String chat5(String city){
        String answer = deepseekChatClient.prompt()
            //AI能力边界
            .user(city + "未来3天天气情况如何？")
            .call()
            .chatResponse()
            .getResult()
            .getOutput()
            .getText();

        ToolResponseMessage toolResponseMessage = new ToolResponseMessage(
            List.of(new ToolResponseMessage.ToolResponse("1","获得天气",city)
            )
        );

        String toolResponse = toolResponseMessage.getText();

        String result = answer + toolResponse;

        return result;
    }





}




