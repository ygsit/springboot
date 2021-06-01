package com.yu.service;

import com.yu.domain.Student;

import java.util.List;

public interface StudentServcice {

    List<Student> listUsers();

    Student getUserById(Integer id);

}
