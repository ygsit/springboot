package com.yu.controller;

import com.yu.entity.Student;
import com.yu.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RabbitController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendMessage")
    public String sendMessage() {
        Student student = new Student("张三", 20, "362330199909195236");
        rabbitTemplate.convertAndSend("java.direct.exchange", "student.key", student);
        log.info("消息发送成功");
        return "success";
    }

    @RequestMapping("/sendMessage10")
    public String sendMessage10() {
        for (int i = 0; i < 10; i++) {
            Student student = new Student("张三" + i, 20, "362330199909195236");
            rabbitTemplate.convertAndSend("java.direct.exchange", "java.key", student);
            log.info("消息{}发送成功", i);
        }
        return "success";
    }

    @RequestMapping("/sendDifferentMessage")
    public String sendDifferentMessage(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        for (int i = 0; i < num; i++) {
            if (i % 2 == 0) {
                Student student = new Student("学生" + i, 20, "362330199909195236");
                rabbitTemplate.convertAndSend("java.direct.exchange", "java.key", student);
            } else {
                User user = new User("用户" + i, "adminPwd");
                rabbitTemplate.convertAndSend("java.direct.exchange", "java.key", user);
            }
            log.info("消息{}发送成功", i);
        }
        return "success";
    }
}
