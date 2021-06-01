package com.yu;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class SpringbootRabbitmqApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建交换机exchange
     */
    @Test
    void createExchange() {
        String exchangeName = "java.direct.exchange";
        Exchange exchange = new DirectExchange(exchangeName);
        amqpAdmin.declareExchange(exchange);
        log.info("Exchange[{}]创建成功", exchangeName);
    }

    /**
     * 创建队列queue
     */
    @Test
    void createQueue() {
        String queueName = "java.queue";
        Queue queue = new Queue(queueName);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功", queueName);
    }

    /**
     * 创建绑定关系binding
     */
    @Test
    void createBinding(){
        // new Binding(String destination,  //目的地
        // Binding.DestinationType destinationType,  //绑定类型
        // String exchange,  //交换机
        // String routingKey, //路由键
        // @Nullable Map<String, Object> arguments  //参数)
        // 把交换机绑定到一个目的地，类型为队列或交换机
        Binding binding = new Binding("java.queue", Binding.DestinationType.QUEUE, "java.direct.exchange", "java.key", null);
        amqpAdmin.declareBinding(binding);
        log.info("绑定关系创建成功");
    }
    
    /**
     * 发送：direct(一对一)
     */
    @Test
    void sendDirectMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "direct");
        map.put("content", "direct内容");
        rabbitTemplate.convertAndSend("test.direct.exchange", "aaa", map);
        System.out.println("发送成功");
    }

    /**
     * 接收消息
     */
    @Test
    void getDirectMessage() {
        Object yu = rabbitTemplate.receiveAndConvert("yu");
        System.out.println(yu);
    }


    /**
     * 发送：fanout(一对多)
     */
    @Test
    void sendFanoutMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "fanout");
        map.put("content", "fanout内容");
        rabbitTemplate.convertAndSend("test.fanout.exchange", null, map);
        System.out.println("发送成功");
    }

    /**
     * 发送：topic(自定义)
     * * (星号) 用来表示一个单词 (必须出现的)
     * # (井号) 用来表示任意数量（零个或多个）单词
     */
    @Test
    void sendTopicMessage() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "topic");
        map.put("content", "topic内容");
		rabbitTemplate.convertAndSend("test.topic.exchange", "yu.key", map);
        System.out.println("发送成功");
    }

}
