package com.yu.mapper;

import com.yu.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * Mapper注解标记该类是一个mybatis的mapper接口，可以被spring boot自动扫描到spring上下文中
 * Mapper注解和Repository区别：
 * Repository注解需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中：
 * 如下，在启动类中配置扫描地址：@MapperScan("com.yu.mapper")  //配置mapper扫描地址
 * Mapper注解不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中。
 */
@Mapper
public interface UserMapper {

    List<User> findAll();
}
