package com.yu.controller;

import com.yu.dao.UserDao;
import com.yu.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserContrller {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "userAdd";
    }

    //增加
    @RequestMapping("/add")
    public String add(User user) {
        userDao.add(user);
        return "redirect:/findAll";
    }


    //根据id查询一个
    @RequestMapping("/findById")
    public String findById(int id, Model model) {
        User user = userDao.findById(id);
        model.addAttribute("user", user);
        return "userUpdate";
    }

    //查询所有
    @RequestMapping("/findAll")
    public String findAll(Model model) {
        List<User> users = userDao.findAll();
        model.addAttribute("list", users);
        return "userList";
    }

    //修改
    @RequestMapping("/update")
    public String update(User user) {
        userDao.update(user);
        return "redirect:/findAll";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(int id) {
        userDao.delete(id);
        return "redirect:/findAll";
    }
}
