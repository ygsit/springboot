package com.yu.service;


import com.yu.domain.HelloProperties;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 默认不要放在容器里
 */
public class HelloService {

    @Autowired
    HelloProperties helloProperties;

    public String hello(String name) {
        return helloProperties.getPrefix() + "<-- " + name + "-- >" + helloProperties.getSuffix();
    }
}
