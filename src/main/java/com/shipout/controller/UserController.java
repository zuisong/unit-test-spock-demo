package com.shipout.controller;

import com.shipout.base.R;
import com.shipout.entity.User;
import com.shipout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public R<List<User>> getUserList() {

        List<User> userList = userService.getUserList();

        return R.ok(userList);

    }


    @GetMapping("/getById")
    public R<User> getUserById(Integer id) {

        User user = userService.findUserById(id);

        return R.ok(user);

    }
    @PostMapping("/addUser")
    R<User> getUserById(@RequestBody User user) {
        return R.ok(null);
    }

}
