package com.yu.controller;

import com.yu.domain.Student;
import com.yu.service.StudentServcice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentServcice studentServcice;

    @RequestMapping("/list")
    public List<Student> listUsers(){
        return studentServcice.listUsers();
    }

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    public Student getUser(@PathVariable("id") Integer id){
        return studentServcice.getUserById(id);
    }
}
