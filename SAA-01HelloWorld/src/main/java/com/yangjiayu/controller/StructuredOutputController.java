package com.yangjiayu.controller;

import com.yangjiayu.records.StudentRecord;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

/**
 * @Classname StructuredOutputController
 * @Description TODO
 * @Date 2025/9/25 11:28
 * @Created by YangJiaYu
 */
@RestController
public class StructuredOutputController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;


    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/structuredoutput/chat")
    public StudentRecord chat(@RequestParam(name = "sname")String sname,
        @RequestParam(name = "email")String email){

        return qwenChatClient.prompt()
            .user(new Consumer<ChatClient.PromptUserSpec>() {
                @Override
                public void accept(ChatClient.PromptUserSpec promptUserSpec) {
                    promptUserSpec.text("学号1001，我叫{sname},大学专业计算机科学与技术邮箱，{email}")
                        .param("sname",sname).param("email",email);
                }
            }).call().entity(StudentRecord.class);

    }


    @GetMapping("/structuredoutput/chat2")
    public StudentRecord chat2(@RequestParam(name = "sname")String sname,
        @RequestParam(name = "email")String email){

        String stringTemplate = """
            学号1002，我叫{sname},大学专业软件工程，邮箱{email}
            """;

        return qwenChatClient.prompt()
            .user(promptUserSpec -> promptUserSpec.text(stringTemplate)
                .param("sname",sname).param("email",email)).call().entity(StudentRecord.class);
    }


}
