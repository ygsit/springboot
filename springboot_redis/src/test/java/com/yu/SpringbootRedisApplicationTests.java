package com.yu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yu.domain.User;
import com.yu.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class SpringbootRedisApplicationTests {

    /**
     * 自定义了RedisTemplate会覆盖原来的RedisTemplate模板
     */
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;


    //注入redisUtils
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void testString() {
        redisTemplate.opsForValue().set("name", "余刚盛");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    public void testSaveUser() throws JsonProcessingException {
        /**
         * redis存对象要序列化：
         *  方法一：把对象转换为Json
         *  方法二：对象实现序列化
         */
        User user = new User("yugangsheng", "123456", "余刚盛");
        //方法一：
        /*String jsonUser = new ObjectMapper().writeValueAsString(user);
        redisTemplate.opsForValue().set("user", jsonUser);*/

        //方法二：
        redisTemplate.opsForValue().set("user", user);
        System.out.println(redisTemplate.opsForValue().get("user"));
    }


    @Test
    public void testRedisUtils(){
        redisUtil.set("key", "yu");
        System.out.println(redisUtil.get("key"));
    }

}
