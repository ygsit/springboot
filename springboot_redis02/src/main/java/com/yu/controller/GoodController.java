package com.yu.controller;

import com.yu.utils.RedisUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
public class GoodController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final Lock lock = new ReentrantLock();

    @Value("${server.port}")
    private String serverPort;

    //定义一个Redis锁常量
    public static final String REDIS_LOCK_KEY = "lock_yu";

    @Autowired
    private Redisson redisson;

    @GetMapping("/buy_goods")
    public String buy_Goods() throws Exception {

        RLock lock = redisson.getLock(REDIS_LOCK_KEY);
        //加锁
        lock.lock();
        try {

            String result = stringRedisTemplate.opsForValue().get("goods:001");
            int goodsNumber = result == null ? 0 : Integer.parseInt(result);

            if (goodsNumber > 0) {
                int realNumber = goodsNumber - 1;
                stringRedisTemplate.opsForValue().set("goods:001", realNumber + "");
                System.out.println("你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort);
                return "你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort;
            } else {
                System.out.println("商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort);
            }


            return "商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort;
        } finally {
            //还在持有锁的状态，并且是当前线程持有的锁再解锁
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }


    private String luaAtomic() throws Exception {
        String value = UUID.randomUUID().toString() + Thread.currentThread().getName();

        try {
            //setIfAbsent()：如果不存在就新建，加锁，并设置一个过期时间
            Boolean ifAbsent = stringRedisTemplate.opsForValue().setIfAbsent(REDIS_LOCK_KEY, value, 10L, TimeUnit.SECONDS);

            if (!ifAbsent) {
                return "抢锁失败！";
            }

            String result = stringRedisTemplate.opsForValue().get("goods:001");
            int goodsNumber = result == null ? 0 : Integer.parseInt(result);

            if (goodsNumber > 0) {
                int realNumber = goodsNumber - 1;
                stringRedisTemplate.opsForValue().set("goods:001", realNumber + "");
                System.out.println("你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort);
                return "你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort;
            } else {
                System.out.println("商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort);
            }


            return "商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort;
        } finally {
            //释放锁
            /*if(stringRedisTemplate.opsForValue().get(REDIS_LOCK_KEY).equalsIgnoreCase(value)){
                stringRedisTemplate.delete(REDIS_LOCK_KEY);
            }*/

            //事务保证原子性
            /*while (true)
            {
                stringRedisTemplate.watch(REDIS_LOCK_KEY); //加事务，乐观锁
                if (value.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(REDIS_LOCK_KEY))){
                    stringRedisTemplate.setEnableTransactionSupport(true);
                    stringRedisTemplate.multi();//开始事务
                    stringRedisTemplate.delete(REDIS_LOCK_KEY);
                    List<Object> list = stringRedisTemplate.exec();
                    if (list == null) {  //如果等于null，就是没有删掉，删除失败，再回去while循环那再重新执行删除
                        continue;
                    }
                }
                //如果删除成功，释放监控器，并且breank跳出当前循环
                stringRedisTemplate.unwatch();
                break;
            }*/

            //lua脚本保证原子性
            Jedis jedis = RedisUtils.getJedis();
            String script = "if redis.call('get', KEYS[1]) == ARGV[1]" + "then "
                    + "return redis.call('del', KEYS[1])" + "else " + "  return 0 " + "end";
            try {
                Object result = jedis.eval(script, Collections.singletonList(REDIS_LOCK_KEY), Collections.singletonList(value));
                if ("1".equals(result.toString())) {
                    System.out.println("------del REDIS_LOCK_KEY success");
                } else {
                    System.out.println("------del REDIS_LOCK_KEY error");
                }
            } finally {
                if (null != jedis) {
                    jedis.close();
                }
            }
        }
    }


    private String tryLock() throws InterruptedException {
        if (lock.tryLock(3L, TimeUnit.SECONDS)) {
            try {
                String result = stringRedisTemplate.opsForValue().get("goods:001");
                int goodsNumber = result == null ? 0 : Integer.parseInt(result);

                if (goodsNumber > 0) {
                    int realNumber = goodsNumber - 1;
                    stringRedisTemplate.opsForValue().set("goods:001", realNumber + "");
                    System.out.println("你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort);
                    return "你已经成功秒杀商品，此时还剩余：" + realNumber + "件" + "\t 服务器端口: " + serverPort;
                } else {
                    System.out.println("商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort);
                }
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println("获取锁失败" + "\t 服务器端口: " + serverPort);
            return "获取锁失败" + "\t 服务器端口: " + serverPort;
        }
        return "商品已经售罄/活动结束/调用超时，欢迎下次光临" + "\t 服务器端口: " + serverPort;
    }

}