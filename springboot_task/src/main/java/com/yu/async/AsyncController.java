package com.yu.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异步任务：后台会新开一个线程去跑
 *  需要在启动类中开启支持异步任务注解 @EnableAsync
 *  在需要异步的方法上加上 @Async
 */
@RestController
public class AsyncController {

    @Autowired
    AsyncService asyncService;

    @RequestMapping("/testAsync")
    public String testAsync(){
        asyncService.hello();
        return "success";
    }
}
