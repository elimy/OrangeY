package com.andy.orange.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/21.
 */

public class JsoupUtil {

    /*
    * 根据url抓取豆瓣的播放源
    * @return：视频播放类型和地址的map
    * */
    public Map<String,String> grab(String mUrl){

        Map<String,String> map=new HashMap<>();
        Document d;

        try {

            //获取到播放地址
            d = Jsoup.connect(mUrl).get();

            Elements element=d.select("a.playbtn");
            for (int i=0;i<element.size();i++){

                Log.d("element",element.get(i).attr("data-cn")+":"+element.get(i).attr("href"));

                //把视频类型和地址用map保存起来
                map.put(element.get(i).attr("data-cn"),matchUrl(element.get(i).attr("href")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /*
    * 从豆瓣电影链接中获取并替换得到电影播放链接
    * @return：电影链接
    * */
    public String matchUrl(String url) {


        String resultUrl = "";

        //https://www.douban.com/link2/?url=http%3A%2F%2Fv.qq.com%2Fx%2Fcover%2Fuiq0rxuywu508qr.html%3Fptag%3Ddouban.movie&subtype=1&type=online-video

        Pattern p = Pattern.compile("http%3(.*)html");
        Matcher m = p.matcher(url);
        while (m.find()) {

            resultUrl = m.group(0);
            Log.d("matchUrl", m.group(0));
        }

        resultUrl = resultUrl.replace("%3A", ":");
        resultUrl = resultUrl.replace("%2F", "/");

        Log.d("resultUrl", resultUrl);

        return resultUrl;
    }

}
