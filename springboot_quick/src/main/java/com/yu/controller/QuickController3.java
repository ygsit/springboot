package com.yu.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 读取yml文件的内容:
 * 方法一：通过@value注解
 * 方法二：通过@ConfigurationProperties注解
 * 需要提供get和set方法，在类上加上注解，并给定前缀
 */
@Controller
@ConfigurationProperties(prefix = "person")
public class QuickController3 {

    private String name;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @RequestMapping("/getValue2")
    @ResponseBody
    //通过@ConfigurationProperties注解注解映射获取值
    public String quick2() {
        return "name:" + name + " age:" + age;
    }
}
