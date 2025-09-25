package com.yangjiayu.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @Classname PromptTemplateController
 * @Description PromptTemplate基本使用，使用占位符设置模板 PromptTemplate
 * @Date 2025/9/25 10:21
 * @Created by YangJiaYu
 */
@RestController
public class PromptTemplateController {


    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;


    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    /**
     * http://localhost:8999/prompttemplate/chat?topic=java&output_format=html&wordCount=200
     * @param topic
     * @param output_format
     * @param wordCount
     * @return
     */
    @GetMapping("/prompttemplate/chat")
    public Flux<String> chat(String topic,String output_format,String wordCount){
        PromptTemplate promptTemplate = new PromptTemplate("" +
            "讲一个关于{topic}的故事" +
            "并以{output_format}格式输出，" +
            "字数在{wordCount}左右");

        // PromptTemplate -> Prompt
        Prompt prompt = promptTemplate.create(
            Map.of(
                "topic",topic,
                "output_format",output_format,
                "wordCount",wordCount
            )
        );

        return deepseekChatClient.prompt(prompt).stream().content();
    }

    // 读取模板文件实现模板功能
    @Value("classpath:/prompttemplate/template1.txt")
    private org.springframework.core.io.Resource userTemplate;//专门读取文件


    /**
     * 模板文件分离
     * @param topic
     * @param output_format
     * @return
     */
    @GetMapping("/prompttemplate/chat2")
    public Flux<String> chat2(String topic,String output_format){
        PromptTemplate promptTemplate = new PromptTemplate(userTemplate);

        Prompt prompt = promptTemplate.create(Map.of("topic", topic, "output_format", output_format));

        return deepseekChatClient.prompt(prompt).stream().content();
    }


    /**
     * http://localhost:8999/prompttemplate/chat3?sysTopic=医生&userTopic=肾虚
     * @param sysTopic
     * @param userTopic
     * @return
     */
    @GetMapping("/prompttemplate/chat3")
    public Flux<String> chat3(String sysTopic,String userTopic){
        //1.SystemPromptTemplate
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
            "你是{systemTopic}助手，只回答{systemTopic}其他无可奉告，以HTML格式的结果返回");
        Message sysMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", sysTopic));

        //2.PromptTemplate
        PromptTemplate UserpromptTemplate = new PromptTemplate("解释一下{userTopic}");
        Message userMessage = UserpromptTemplate.createMessage(Map.of("userTopic", userTopic));

        //3.组合【关键】多个Message -> Prompt
        Prompt prompt = new Prompt(List.of(sysMessage, userMessage));
        return deepseekChatClient.prompt(prompt).stream().content();
    }


    /**
     * 示例地址：
     * http://localhost:8999/prompttemplate/chat4?question=牡丹花
     */
    @GetMapping("/prompttemplate/chat4")
    public String chat4(String question) {
        // 1. 系统消息
        SystemMessage systemMessage = new SystemMessage(
            "你是一个Java编程助手，拒绝回答非技术问题。");

        // 2. 用户消息
        UserMessage userMessage = new UserMessage(question);

        // 3. 组装 Prompt
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 4. 调用大模型
        String result = deepseekChatModel
            .call(prompt)
            .getResult()
            .getOutput()
            .getText();

        System.out.println(result);
        return result;
    }

    /**
     * 示例地址：
     * http://localhost:8086/prompttemplate/chat5?question=火锅
     */
    @GetMapping("/prompttemplate/chat5")
    public Flux<String> chat5(String question) {
        return deepseekChatClient.prompt()
            .system("你是一个Java编程助手，拒绝回答非技术问题。")
            .user(question)
            .stream()
            .content();
    }


}
