package com.yu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    //访问swagger： localhost:8080/swagger-ui.html
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello swagger";
    }

}
