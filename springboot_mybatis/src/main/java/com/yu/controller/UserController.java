package com.yu.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.domain.User;
import com.yu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("showAll")
    @ResponseBody
    public String showAll() throws JsonProcessingException {
        List<User> userList = userMapper.findAll();
        for (User user : userList) {
            System.out.println(user);
        }
        //springboot自动集成了jackson
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(userList);
        return json;
    }
}
