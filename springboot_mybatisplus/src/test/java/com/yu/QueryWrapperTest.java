package com.yu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.domain.Student;
import com.yu.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * 条件构造器
 */
@SpringBootTest
public class QueryWrapperTest {

    @Autowired
    private StudentMapper studentMapper;

    // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于20
    @Test
    void test1(){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        //链式编程
        wrapper.isNotNull("name").isNotNull("email").ge("age", 20);
        List<Student> Students = studentMapper.selectList(wrapper);
        Students.forEach(System.out::println);
    }


    // 查询名字yu
    @Test
    void test2(){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "yu");
        Student Student = studentMapper.selectOne(wrapper);
        System.out.println(Student);
    }


    // 查询年龄在 20 ~ 30 岁之间的用户
    @Test
    void test3(){
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.between("age", 20, 30);
        Integer count = studentMapper.selectCount(wrapper);
        System.out.println(count);
    }


    // 模糊查询，查询名字里面不含'e',邮箱有't'的用户
    @Test
    void test4() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.notLike("name", "e").like("email", "t");
        List<Map<String, Object>> maps = studentMapper.selectMaps(wrapper);
        maps.forEach(System.out::println);
    }

    //id 在子查询中查出来
    @Test
    void test5() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.inSql("id", "select id from Student where id<3");
        List<Student> Students = studentMapper.selectList(wrapper);
        Students.forEach(System.out::println);
    }

    // 通过id进行排序并分页
    @Test
    void test6() {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        Page<Student> page = new Page<>(1, 3);
        Page<Student> selectPage = studentMapper.selectPage(page, wrapper);
        System.out.println(selectPage.getRecords());
    }

}
