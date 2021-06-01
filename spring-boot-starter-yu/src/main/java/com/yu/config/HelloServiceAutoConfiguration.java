package com.yu.config;

import com.yu.domain.HelloProperties;
import com.yu.service.HelloService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(HelloService.class)
@EnableConfigurationProperties(HelloProperties.class) //会默认把HelloProperties放到容器中
public class HelloServiceAutoConfiguration {

    @Bean
    public HelloService helloServiceAutoConfiguration(){
        return new HelloService();
    }
}
