package com.shipout.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shipout.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    void batchSaveUser(@Param("userList") List<User> userList);

    default void insertUser(@Param("user") User user) {
        if (user == null) {
            return;
        } else {
            insertUserNotNull(user);
        }
    }

    void insertUserNotNull(User user);
}
