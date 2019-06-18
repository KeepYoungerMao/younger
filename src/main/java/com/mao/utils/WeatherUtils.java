package com.mao.utils;

import com.alibaba.fastjson.JSON;
import com.mao.utils.bean.weather.ForecastWeather;
import com.mao.utils.bean.weather.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * 天气获取工具类，
 * @author mao by 14:24 2019/6/18
 */
public class WeatherUtils {

    private static final String WEATHER_URL = "http://wthrcdn.etouch.cn/weather_mini?city=";

    /**
     * 获取天气信息
     * 并对天气信息进行处理
     * @param city 省市
     * @return 天气信息类
     */
    public static Result getWeather(String city){
        String resultData = getWeatherData(city);
        if(SU.isNotEmpty(resultData)){
            try {
                Result result = JSON.parseObject(resultData, Result.class);
                if("OK".equals(result.getDesc())){
                    return translateWeatherData(result);
                }
            }catch (Exception e){
                e.printStackTrace();
                return new Result(404,"NO");
            }
        }
        return new Result(404,"NO");
    }

    /**
     * 修缮数据
     * @param result 天气数据
     * @return result
     */
    private static Result translateWeatherData(Result result){
        result.getData().getYesterday().setFl(removeK(result.getData().getYesterday().getFl()));
        for(ForecastWeather weather : result.getData().getForecast()){
            weather.setFengli(removeK(weather.getFengli()));
        }
        return result;
    }

    /**
     * 去除<![CDATA[]]>
     * @param str 字符串
     * @return str
     */
    private static String removeK(String str){
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(str);
        if(m.matches()){
            return m.group(1);
        }
        return str;
    }


    /**
     * 获取天气信息
     * 网址为：http://wthrcdn.etouch.cn/weather_mini
     * @param city 省市
     * @return 信息数据JSON串
     */
    private static String getWeatherData(String city) {

        URL realUrl;
        ByteArrayOutputStream out = null;

        try {
            //真实地址
            realUrl = new URL(WEATHER_URL+city);

            //打开连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            //设置连接属性
            connection.setRequestProperty("accept",
                    "application/xhtml+xml,application/json,application/xml;" +
                            "charset=utf-8, text/javascript, */*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("contentType", "utf-8");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");

            //这里获取的数据时压缩格式的数据所以用gzip进行解压缩
            GZIPInputStream gip = new GZIPInputStream(connection.getInputStream());
            out = new ByteArrayOutputStream();
            //缓冲
            byte[] buffer = new byte[1024];
            int len ;
            while((len = gip.read(buffer))!=-1){
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            //关闭流
            try {
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String result = null;
        try{
            result = new String(out.toByteArray(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        //把字节数据转化为字符串返回回去
        return result;
    }

    public static void main(String[] args){
        System.out.println(getWeather("重庆"));
    }

}