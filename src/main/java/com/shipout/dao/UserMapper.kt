package com.shipout.dao

import com.baomidou.mybatisplus.core.mapper.*
import com.shipout.entity.*
import org.apache.ibatis.annotations.*

interface UserMapper : BaseMapper<User> {
    fun batchSaveUser(@Param("userList") userList: List<User>)

    fun insertUserNotNull(user: User)
}
