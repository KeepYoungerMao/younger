package com.mao.entity.etc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mao by 15:07 2019/12/18
 */
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    private Long id;            //id
    private String name;        //name
    private String image;       //background image
    private String actor;       //演员列表
    private String type;        //类型
    private String time;        //上映时间
    private String place;       //上映地点
    private String weight;      //画质
    private String intro;       //简介
    private String m3u8;        //直播源

    private String html;

    public Movie(String name, String image, String actor, String type,
                 String time, String weight, String html){
        this.id = 0L;
        this.name = name;
        this.image = image;
        this.actor = actor;
        this.type = type;
        this.time = time;
        this.weight = weight;
        this.html = html;
    }
}