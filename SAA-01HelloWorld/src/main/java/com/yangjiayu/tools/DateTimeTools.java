package com.yangjiayu.tools;

import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

/**
 * @Classname DateTimeTools
 * @Description TODO
 * @Date 2025/9/27 20:51
 * @Created by YangJiaYu
 */
public class DateTimeTools {

    /**
     * 1.定义function call(tool call)
     * 2.returnDirect
     *  true = tool直接不走大模型，直接给客户
     *  false = 默认值，拿到tool返回的结果，给大模型，最后由大模型回复
     * @return
     */
    @Tool(description = "获取当前时间",returnDirect = false)
    public String getCurrentTime(){
        return LocalDateTime.now().toString();
    }
}
