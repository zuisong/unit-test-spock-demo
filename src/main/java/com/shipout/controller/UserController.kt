package com.shipout.controller

import com.shipout.base.*
import com.shipout.entity.*
import com.shipout.service.*
import org.springframework.beans.factory.annotation.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/list")
    fun getUserList(): R<List<User>?> {
        val userList = userService.getUserList()
        return R.Companion.ok<List<User>?>(userList)
    }

    @GetMapping("/getById")
    fun getUserById(id: Int): R<User?> {
        val user = userService!!.findUserById(id)
        return R.Companion.ok<User?>(user)
    }

    @PostMapping("/addUser")
    fun addUser(@RequestBody user: User?): R<User?> {
        return R.Companion.ok<User?>(null)
    }
}
