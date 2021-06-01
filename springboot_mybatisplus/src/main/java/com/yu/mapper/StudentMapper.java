package com.yu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yu.domain.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    Student getUserById(Integer id);

}
