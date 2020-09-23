package com.shipout.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipout.dao.UserMapper;
import com.shipout.entity.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class Userservice {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public User findUserById(int id) {

        String userString = stringRedisTemplate.opsForValue().get(id);
        if (Objects.isNull(userString)) {
            User user = userMapper.selectById(id);
            userString = objectMapper.writeValueAsString(user);
            stringRedisTemplate.opsForValue().set(String.valueOf(id), userString, 10, TimeUnit.MINUTES);
            return user;
        } else {
            return objectMapper.readValue(userString, User.class);
        }


    }

    public List<User> getUserList() {
        return userMapper.selectList(null);
    }
}
