package com.mao.utils.bean.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 天气具体信息类
 * @author mao by 10:11 2018/8/31
 */
@Getter
@Setter
@ToString
public class WeatherData {

    private YesterdayWeather yesterday;         //昨日天气情况
    private String city;                        //城市
    private String aqi;                         //城市id
    private List<ForecastWeather> forecast;     //未来天气情况
    private String ganmao;                      //预防信息
    private String wendu;                       //平均温度
}