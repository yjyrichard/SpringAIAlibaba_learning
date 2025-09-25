package com.yangjiayu.controller;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname Text2ImageController
 * @Description TODO
 * @Date 2025/9/25 20:55
 * @Created by YangJiaYu
 */
@RestController
public class Text2ImageController {

    //img model
    public static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";

    @Autowired
    private ImageModel imageModel;

    @GetMapping(value = "/t2i/image")
    public String image(@RequestParam(name = "prompt",defaultValue = "刺猬")String prompt){
        return imageModel.call(
            //withN withHight
         new ImagePrompt(prompt, DashScopeImageOptions.builder().withModel(IMAGE_MODEL).build())
        )
            .getResult()
            .getOutput()
            .getUrl();//https://dashscope-result-wlcb-acdr-1.oss-cn-wulanchabu-acdr-1.aliyuncs.com/1d/fb/20250925/1445597e/85cf66e2-e100-4f16-bd99-f424cac004931857852116.png?Expires=1758891781&OSSAccessKeyId=LTAI5tKPD3TMqf2Lna1fASuh&Signature=69vulLoWqosIVe79yl8PBDhBk%2Bc%3D
    }

}









