package com.shipout.controller;

import com.shipout.base.R;
import com.shipout.entity.User;
import com.shipout.service.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private Userservice userservice;


    @GetMapping("/list")
    public R<List<User>> getUserList() {

        List<User> userList = userservice.getUserList();

        return R.ok(userList);

    }


    @GetMapping("/getById")
    public R<User> getUserById(Integer id) {

        User user = userservice.findUserById(id);

        return R.ok(user);

    }


}
