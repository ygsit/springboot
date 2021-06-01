package com.yu.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 1、引入邮箱依赖：
 *      <dependency>
 *          <groupId>org.springframework.boot</groupId>
 *          <artifactId>spring-boot-starter-mail</artifactId>
 *      </dependency>
 * 2、application.properties配置:
 *      spring.mail.username=183213557@qq.com
 *      spring.mail.password=kelbqcgajhhibgbj
 *      spring.mail.host=smtp.qq.com
 *      spring.mail.properties.mail.smtp.ssl.enable=true
 * 3、注入JavaMailSenderImpl类
 */
@RestController
public class MailController {
    @Autowired
    JavaMailSenderImpl mailSender;


    //发送简单邮件
    @RequestMapping("sendSimpleMail")
    public String sendSimpleMail(){
        //创建一个简单的消息邮件
        SimpleMailMessage message = new SimpleMailMessage();
        //邮件设置
        message.setSubject("通知-今晚开会");
        message.setText("今晚7:30开会");
        message.setTo("ygsity@163.com");
        message.setFrom("183213557@qq.com");
        //发送邮件
        mailSender.send(message);
        return "success";
    }

    //发送复杂邮件
    @RequestMapping("sendComplexMail")
    public String sendComplexMail() throws MessagingException {
        //创建一个复杂的消息邮件
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        //邮件设置
        helper.setSubject("通知-今晚开会");
        helper.setText("<b style='color:red'>今晚7:30开会</b>", true);
        helper.setTo("ygsity@163.com");
        helper.setFrom("183213557@qq.com");
        //上传文件
        helper.addAttachment("1.jpg",new File("E:\\Picture\\Pictures\\1.jpg"));
        helper.addAttachment("2.jpg",new File("E:\\Picture\\Pictures\\2.jpg"));
        //发送邮件
        mailSender.send(message);
        return "success";
    }
}
