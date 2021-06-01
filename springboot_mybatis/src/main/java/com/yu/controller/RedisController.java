package com.yu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.domain.User;
import com.yu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 需要注入RedisTemplate，
 * 使用redisTemplate.boundValueOps的get和set方法
 */
@Controller
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/redisTest")
    @ResponseBody
    public String redisTest() throws JsonProcessingException {
        //从缓存中获取数据
        String redisData = redisTemplate.boundValueOps("user.findAll").get();
        if (redisData == null) {
            //缓存中没有数据，从数据库查询
            List<User> users = userMapper.findAll();
            ObjectMapper mapper = new ObjectMapper();
            redisData = mapper.writeValueAsString(users);
            //存到缓存中
            redisTemplate.boundValueOps("user.findAll").set(redisData);
            System.out.println("===============从数据库获得数据===============");
            return redisData;
        } else {
            System.out.println("===============从缓存中获得数据===============");
            //有数据就直接返回
            return redisData;
        }
    }
}
