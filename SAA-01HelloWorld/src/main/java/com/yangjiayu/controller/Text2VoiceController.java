package com.yangjiayu.controller;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisMessage;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * @Classname Text2VoiceController
 * @Description 文生音
 * @Date 2025/9/25 21:22
 * @Created by YangJiaYu
 */
@RestController
public class Text2VoiceController {

    @Resource
    private SpeechSynthesisModel  speechSynthesisModel;

    // voice model
    public static final String BAILIAN_VOICE_MODEL = "cosyvoice-v2";
    // voice timber 音色列表：
    public static final String BAILIAN_VOICE_TIMBER = "longyingcui";// 龙应催

    @GetMapping("/t2v/voice")
    public String voice(@RequestParam(name = "msg",defaultValue = "温馨提醒，支付宝到账100元")String msg){
        String filePath = "d:\\" + UUID.randomUUID().toString() + ".mp3";

        //1.语音参数设置
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
            .model(BAILIAN_VOICE_MODEL)
            .voice(BAILIAN_VOICE_TIMBER)
            .build();

        //2.调用大模型语音生成对象
        SpeechSynthesisResponse reponse = speechSynthesisModel.call(new SpeechSynthesisPrompt(msg,options));


        //3.字节流语音转换
        ByteBuffer byteBuffer = reponse.getResult().getOutput().getAudio();

        // 将在内存的字节流写在硬盘上
        try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
            fileOutputStream.write(byteBuffer.array());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        // 生成路径
        return filePath;//d:\e2b31c4c-db2b-413c-a0f3-764092880983.mp3
    }
}
