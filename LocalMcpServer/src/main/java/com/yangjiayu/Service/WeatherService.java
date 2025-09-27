package com.yangjiayu.Service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Classname WeatherService
 * @Description TODO
 * @Date 2025/9/27 21:54
 * @Created by YangJiaYu
 */
@Service
public class WeatherService {

    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherByCity(String city){
        Map<String, String> map = Map.of(
            "北京", "11111111111111111111111",
            "上海", "22222222222222222222222",
            "深圳", "33333333333333333333333"
        );

        return map.getOrDefault(city,"抱歉，找不到");
    }
}
