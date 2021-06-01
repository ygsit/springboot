package com.yu.service.impl;

import com.yu.domain.Student;
import com.yu.mapper.StudentMapper;
import com.yu.service.StudentServcice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class StudentServiceImpl implements StudentServcice {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> listUsers() {
        return studentMapper.selectList(null);
    }

    @Override
    public Student getUserById(Integer id) {
        return studentMapper.getUserById(id);
    }
}
