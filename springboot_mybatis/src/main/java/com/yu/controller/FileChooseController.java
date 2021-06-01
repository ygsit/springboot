package com.yu.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileChooseController {

    @Value("${environment}")
    private String environment;

    @Value("${person.name}")
    private String name;

    @Value("${person.age}")
    private Integer age;

    @RequestMapping("/choose")
    @ResponseBody
    public String choose() {
        return "环境是：" + environment + "， 姓名：" + name + "， 年龄：" + age;
    }
}
