package com.yu.dao;

import com.yu.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    //增加
    void add(User user);

    //根据id查询一个
    User findById(int id);

    //查询所有
    List<User> findAll();

    //修改
    void update(User user);

    //删除
    void delete(int id);

}
