package com.parse.util;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/***
 * 谷歌翻译接口不是开源的
 */
public class TranslateUtil {
    private static String TRANSLATE_URL = "https://translate.google.cn/translate_a/single?client=webapp&sl=auto&tl=zh-CN&hl=zh-CN&dt=at&dt=bd" +
            "&dt=ex&dt=ld&dt=md&dt=qca&dt=rw&dt=rm&dt=sos&dt=ss&dt=t&otf=1&ssel=0&tsel=3&xid=45662847%2C45683275&kc=1&tk=141300.320519&q=";
    public static HttpResponse tancLateEnglish2Chinese(String english) throws IOException {
        String url = TRANSLATE_URL + "+" + english;
        HttpGet get = new HttpGet(url);
        HttpClient client = new DefaultHttpClient();
        get.setHeader("authority","translate.google.cn");
        get.setHeader("method","GET");
        get.setHeader("scheme","https");
        get.setHeader("accept","*/*");
        get.setHeader("accept-encoding","gzip, deflate, br");
        get.setHeader("accept-encoding","gzip, deflate, br");
        get.setHeader("accept-language","zh-CN,zh;q=0.9");
        get.setHeader("cache-control","no-cache");
        get.setHeader("pragma","no-cache");
        get.setHeader("referer","https://translate.google.cn/");
        get.setHeader("sec-fetch-dest","empty");
        get.setHeader("sec-fetch-mode","cors");
        get.setHeader("sec-fetch-site","same-origin");
        get.setHeader("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36");
        get.setHeader("x-client-data","CIS2yQEIprbJAQjBtskBCKmdygEImLXKAQiZvcoBCObGygEI58jKAQi0y8oB");
        HttpResponse res = client.execute(get);
        return res;
    }

    public static void main(String[] args) throws IOException {
        HttpResponse response = TranslateUtil.tancLateEnglish2Chinese("English");
        System.out.println(response);
        System.out.println(response.getStatusLine().getStatusCode());
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            System.out.println("成功");
            String ret = EntityUtils.toString(response.getEntity(),"UTF-8");
            System.out.println(ret);
        }
    }
}
