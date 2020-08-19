package com.spring;

import org.springframework.util.AntPathMatcher;

import java.util.Map;
import java.util.regex.Pattern;

// TODO: 2020-04-21  这个mian函数主要是为了测试 AntPathMatcher 这个类的一些用法
public class Application {
    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        // 这个类是一个资源路径匹配使用的规则类
        // 精准模式匹配 完全匹配规格
        boolean res = antPathMatcher.match("/aa/bb/cc/dd/ee/ff/1.txt","/aa/bb/cc/dd/ee/ff/1.txt");
        System.out.println(res);
        // 严格模式匹配  ?表示任意一个字符，一个占位符
        res = antPathMatcher.match("/aa/b?/cc/dd/ee/ff/1.txt","/aa/bb/cc/dd/ee/ff/1.txt");
        System.out.println(res);

        // * 匹配
        res = antPathMatcher.match("/aa/*/cc/dd/ee/ff/1.txt","/aa/bb/cc/dd/ee/ff/1.txt");
        System.out.println(res);

        // ** 匹配
        res = antPathMatcher.match("/aa/**/1.txt","/aa/bb/cc/dd/ee/ff/1.txt");
        System.out.println(res);

        Map<String,String> map = antPathMatcher.extractUriTemplateVariables("aa/bb/{path}/{path1}","/aa/bb/cc/dd");
        System.out.println(map);

        map = antPathMatcher.extractUriTemplateVariables("/aa/bb/{path1:\\w}/{path2:\\d+}/","/aa/bb/cc/123");
        System.out.println(map);

        Pattern reg = Pattern.compile("a.*");
        reg.matcher("aa").find();   // 不完全匹配
        reg.matcher("aa").matches();  // 完全匹配
    }
}
