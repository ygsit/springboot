package com.yu.service;

import com.rabbitmq.client.Channel;
import com.yu.entity.Student;
import com.yu.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RabbitListener(queues = {"java.queue"})
public class RabbitService {

    /**
     * 监听消息：使用@RabbitListener：必须先开启rabbitmq支持(@EnableRabbit)，且必须作用在被spring托管的类上
     *  @RabbitListener：标在类+方法上(监听队列)，可以配合@RabbitHandler使用
     *  @RabbitHandler：标在方法上(重载区分不用的消息)
     *
     * 方法参数：
     *  1.Message message: 原生消息类型 详细信息
     * 	2.T<发送消息的类型> Student student  [Spring自动帮我们转换]
     * 	3.Channel channel: 当前传输数据的通道
     *
     * 消息处理：
     *  1、只有一个消息处理完成，方法运行结束，才能接收下一个消息
     *  2、当有多个服务监听同一个消息队列的时候，同一个消息只能由一个服务接收到
     */
//    @RabbitListener(queues = {"java.queue"})
    @RabbitHandler
    void listenStudentMessage(Message message, Student student, Channel channel){
//        log.info("接收到消息message：" + message);
        log.info("接收到消息student：" + student);
//        log.info("接收到消息channel：" + channel);

        log.info("消息处理完成！");
    }

    @RabbitHandler
    void listenUserMessage(User user){
        log.info("接收到用户消息：" + user);
        log.info("用户消息处理完成！");
    }
}
