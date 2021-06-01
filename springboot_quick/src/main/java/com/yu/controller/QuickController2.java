package com.yu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 读取yml文件的内容:
 * 方法一：通过@value注解
 * 方法二：通过@ConfigurationProperties注解
 */
@Controller
public class QuickController2 {

    @Value("${name}")
    private String name;

    @Value("${person.age}")
    private String age;

    @RequestMapping("/getValue")
    @ResponseBody
    //通过@value注解映射获取值
    public String quick2() {
        return "name:" + name + " age:" + age;
    }
}
