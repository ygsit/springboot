package com.yu;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yu.domain.Student;
import com.yu.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringbootMybatisplusApplicationTests {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 插入:id会自动分配（雪花算法）
     *  让id自动增长：
     *      1、实体类字段上 @TableId(type = IdType.AUTO)
     *      2、数据库字段一定要是自增！
     *  自动填充：一般用于创建时间，更新时间
     *      1.数据库添加字段
     *      2.在实体类字段上加注解：
     *          创建时间：@TableField(fill = FieldFill.INSERT)
     *          更新时间：@TableField(fill = FieldFill.INSERT_UPDATE)
     *      3.编写处理器来处理这个注解
     */
    @Test
    void testInsert(){
        Student Student = new Student();
        Student.setName("test12");
        Student.setAge(20);
        Student.setEmail("183213557@qq.com");
        studentMapper.insert(Student);
    }


    /**
     * 更新操作:UPDATE Student SET name=?, age=?, email=? WHERE id=?
     */
    @Test
    void testUpdate(){
        Student Student = new Student();
        Student.setId(7);
        Student.setName("gang");
        Student.setAge(20);
        Student.setEmail("183213557@qq.com");
        // 注意：updateById, 但是参数是一个对象！
        studentMapper.updateById(Student);
    }


    /**
     * 删除操作
     */
    @Test
    void testDelete(){
        studentMapper.deleteById(1277070328090161163l);
    }


    /**
     * 逻辑删除：
     *     1.数据库字段增加字段
     *     2.实体类添加属性，加上@TableLogic注解
     *     3.在配置文件配置逻辑删除
     *     4.在配置类中配置逻辑删除组件(在3.1.1版本之后不需要这一步)
     */
    @Test
    void testDeleteByLogic(){
        studentMapper.deleteById(9L);
    }


    /**
     * 乐观锁：
     *  乐观锁实现方式：
     *      取出记录时，获取当前 version
     *      更新时，带上这个version
     *      执行更新时， set version = newVersion where version = oldVersion
     *      如果version不对，就更新失败
     *  代码实现：
     *      1.数据库添加字段
     *      2.实体类添加属性，加@Version注解
     *      3.在配置类中注册组件
     */
    //测试乐观锁成功
    @Test
    void testOptimisticLocker(){
        // 1、查询用户信息
        Student Student = studentMapper.selectById(8L);
        // 2、修改用户信息
        Student.setName("sheng");
        // 3、执行更新操作
        studentMapper.updateById(Student);
    }


    //测试乐观锁失败
    @Test
    void testOptimisticLockerFail(){
        // 线程一
        Student Student = studentMapper.selectById(8L);
        Student.setName("shengsheng");
        // 模拟另外一个线程执行了插队操作
        Student Student2 = studentMapper.selectById(8L);
        Student2.setName("sheng");
        //提交更新操作
        studentMapper.updateById(Student2);
        studentMapper.updateById(Student);// 如果没有乐观锁就会覆盖插队线程的值
    }


    /**
     * 根据id查询
     */
    @Test
    void testSelectOne(){
        Student Student = studentMapper.selectById(1);
        System.out.println(Student);
    }


    /**
     * 查询所有
     */
    @Test
    void testSelectList() {
        List<Student> Students = studentMapper.selectList(null);
        Students.forEach(System.out::println);
    }


    /**
     * 批量查询
     */
    @Test
    void testSelectBatchIds(){
        List<Student> Students = studentMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        Students.forEach(System.out::println);
    }


    /**
     * 按条件查询：使用map
     */
    @Test
    void testSelectByMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("age",20);
        studentMapper.selectByMap(map);
    }


    /**
     * 分页查询：需要在配置类中注册分页插件
     *     直接使用Page对象即可
     */
    @Test
    void testSelectPage(){
        // 参数一：当前页
        // 参数二：页面大小
        Page<Student> page = new Page(2, 3);
        Page<Student> result = studentMapper.selectPage(page, null);
        //使用page对象获得分页结果
//        System.out.println("当前页"+page.getCurrent());
        System.out.println("当前页"+result.getCurrent());
//        System.out.println("一页显示的记录数"+page.getSize());
        System.out.println("一页显示的记录数"+result.getSize());
//        System.out.println("总页数"+page.getPages());
        System.out.println("总页数"+result.getPages());
//        System.out.println("总记录数"+page.getTotal());
        System.out.println("总记录数"+result.getTotal());
        List<Student> Students = result.getRecords();
        Students.forEach(System.out::println);
    }
}
