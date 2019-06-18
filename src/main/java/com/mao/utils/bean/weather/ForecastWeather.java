package com.mao.utils.bean.weather;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 未来天气信息类
 * @author mao by 10:15 2018/8/31
 */
@Getter
@Setter
@ToString
public class ForecastWeather {

    private String date;            //日期
    private String high;            //最高温度
    private String fengli;          //风力
    private String low;             //最低温度
    private String fengxiang;       //风向
    private String type;            //天气情况
}