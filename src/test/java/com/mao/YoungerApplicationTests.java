package com.mao;

import com.alibaba.fastjson.JSON;
import com.mao.entity.etc.M3U8;
import com.mao.entity.etc.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YoungerApplicationTests {

	//爬取的网站头
	private static final String BASE = "";
	//电影分类：第一页前缀
	private static final String SRC_PRE = "/?m=vod-type-id-1-pg-";
	//电影分类：第一页后缀
	private static final String SRC_FIX = ".html";
	//电影分类：总页数（请实时观察情况：网站每天会更新）
	private static final int COUNT = 731;

	@Test
	public void contextLoads() {

    }

	/**
	 * 目前已爬至 361
	 */
	@Test
	public void getMovie(){
		for (int i = 1; i <= COUNT; i++) {
			//拼凑原地址
			String src = BASE+SRC_PRE+i+SRC_FIX;
			//寻找电影
			findMovie(src);
		}
	}
	private void findMovie(String url){
		Document document;
		try {
			document = Jsoup.connect(url).timeout(20000).get();
		} catch (IOException e) {
			document = null;
		}
		//document为html对象
		if (null != document){
			//执行js，获取列表标签
			Elements elements = document.select(".main .index-area ul li a");
			if (nn(elements)){
				for (Element element : elements) {
					String next_url;            //详情页面链接
					String name = "",image = "";          //背景图片
					String actor = "";          //演员列表
					String type = "";           //电影类型
					String time = "";           //时间/地点
					String gg = "";             //电影格式
					next_url = element.attr("href");
					Elements imgBox = element.select("img");
					if (nn(imgBox)){
						image = imgBox.first().attr("data-original");
					}
					Elements pList = element.select("span.lzbz p");
					if (nn(pList)){
						name = pList.first().text();
						if (pList.size() >= 4){
							type = pList.get(2).text();
							actor = pList.get(1).text();

							time = pList.get(3).text();
						} else {
							time = pList.get(pList.size() - 1).text();
							if (pList.size() >= 3)
								type = pList.get(pList.size() - 2).text();
						}
					}
					Elements ggBox = element.select(".other");
					if (nn(ggBox))
						gg = ggBox.first().text();
					findM3U8Html(new Movie(name,image,actor,type,time,gg,next_url));
				}
			}
		}else {
			System.out.println("网站打不开");
		}
	}

	private void findM3U8Html(Movie movie){
		System.out.print("【电影获取】：正在处理电影 -> "+movie.getName());
		Document document;
		try {
			document = Jsoup.connect(BASE+movie.getHtml()).timeout(20000).get();
		} catch (Exception e) {
			document = null;
		}
		if (null != document){
			Elements eeBox = document.select(".main .ct .ct-c .ee");
			if (nn(eeBox)){
				movie.setIntro(splitToIntro(eeBox.first().attr("ee")));
			}
			Elements select = document.select("#stab1 .tab8 ul li");
			if (nn(select)){
				for (Element element : select) {
					String selectName = element.text().trim();
					boolean get = false;

					//只获取m3u8
					if ("在线播放".equals(selectName)){
						String id = element.attr("id");
						Elements as = document.select("#s" + id + " ul li a");
						if (nn(as)){
							String next_url = as.first().attr("href");
							movie.setHtml(next_url);
							get = true;
							//去获取m3u8地址的页面
							findM3U8(movie);
						}
					}
					if (get)
						break;
				}
			}else {
				System.out.println("\t【无播放地址】");
			}
		}else {
			System.out.println("\t【无播放地址】");
		}
	}

	private void findM3U8(Movie movie) {
		String m3u8 = null;
		try {
			m3u8 = getM3U8(BASE + movie.getHtml());
		} catch (Exception e) {
			System.out.print("【无有效页面】");
		}
		if (null == m3u8)
			System.out.print("【m3u8获取失败】");
		else {
			System.out.print("\t【m3u8获取成功】：√√√√√√√√√√√ ");
			movie.setM3u8(splitToM3U8(m3u8));
			//保存
			//searchMapper.saveMovie(movie);
		}
		System.out.println();
	}

	private String getM3U8(String url) {
		Document document;
		try {
			document = Jsoup.connect(url).timeout(20000).get();
		} catch (IOException e) {
			document = null;
		}
		if (null != document){
			String html = document.select(".player .main script").first().html();
			html = html.split("mac_url=")[1];
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine js = manager.getEngineByExtension("js");
			String eval = null;
			try {
				eval = js.eval(html).toString();
			} catch (ScriptException ignored) {}
			if (null != eval)
				return eval;
		}
		return null;
	}

	/**
	 * 判断是否为空
	 * @param elements elements
	 * @return bool
	 */
	private boolean nn(List<Element> elements){
		return null != elements && elements.size() > 0;
	}

	/**
	 * 里面包含部分网站信息
	 * @param str 简介
	 * @return 简介
	 */
	private String splitToIntro(String str){
		if (str.contains("祝你观片愉快！")){
			String[] split = str.split("祝你观片愉快！");
			if (split.length > 1)
				return split[1];
			return str;
		}
		return str;
	}

	/**
	 * 截取m3u8链接
	 * @param str 字符串
	 * @return m3u8链接列表
	 */
	private String splitToM3U8(String str){
		List<M3U8> list;
		if (str.contains("#")){
			list = split0(str.split("#"));
		} else {
			list = split0(new String[]{str});
		}
		return JSON.toJSONString(list);
	}

	private List<M3U8> split0(String[] ss){
		List<M3U8> list = new ArrayList<>();
		for (String s : ss) {
			if (s.contains("$$$")){
				list.addAll(split1(s.split("[$][$][$]")));
			} else {
				list.addAll(split1(new String[]{s}));
			}
		}
		return list;
	}

	private List<M3U8> split1(String[] ss){
		List<M3U8> list = new ArrayList<>();
		for (String s : ss) {
			if (s.contains("$")){
				String[] split = s.split("[$]");
				if (split[1].contains(".m3u8"))
					list.add(new M3U8(split[0],split[1]));
			}
		}
		return list;
	}

}
