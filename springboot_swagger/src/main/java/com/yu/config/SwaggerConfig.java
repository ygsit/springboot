package com.yu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration  //表明是一个配置类
@EnableSwagger2 //开启Swagger2的自动配置
public class SwaggerConfig {

    //配置swagger
    /*@Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        Contact DEFAULT_CONTACT = new Contact("余刚盛", "www.baidu.com", "183213557@qq.com");
        return new ApiInfo("余刚盛的swagger",
                "Api 余刚盛",
                "v1.0",
                "www.baidu.com",
                DEFAULT_CONTACT,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }*/
}
