package com.yu.utils;

import com.yu.entity.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫工具类
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws IOException {
        parseJD("java").forEach(System.out::println);
    }

    public static List<Content> parseJD(String keywords) throws IOException {
        //获取请求：https://search.jd.com/Search?keyword=java
        //前提需要联网
        String url = "https://search.jd.com/Search?keyword=" + keywords;

        //解析网页
        Document document = Jsoup.parse(new URL(url), 30000);
        Element divElement = document.getElementById("J_goodsList");
        Elements lis = divElement.getElementsByTag("li");

        List<Content> contents = new ArrayList<>();

        for (Element li : lis) {
            String title = li.getElementsByClass("p-name").eq(0).text();
            // 注意：懒加载的图片
            String imgUrl = li.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = li.getElementsByClass("p-price").eq(0).text();
            contents.add(new Content(title, imgUrl, price));
        }

        return contents;
    }
}
