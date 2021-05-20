package com.shipout.service

import com.fasterxml.jackson.core.*
import com.fasterxml.jackson.databind.*
import com.shipout.dao.*
import com.shipout.entity.*
import org.springframework.beans.factory.annotation.*
import org.springframework.data.redis.core.*
import org.springframework.stereotype.*
import java.util.*
import java.util.concurrent.*

@Service
class UserService {
    @Autowired
    private var userMapper: UserMapper? = null

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    fun findUserById(id: Int): User? {
        return try {
            var userString = stringRedisTemplate.opsForValue()[id]
            if (Objects.isNull(userString)) {
                val user = userMapper!!.selectById(id)
                userString = objectMapper!!.writeValueAsString(user)
                stringRedisTemplate.opsForValue().set(id.toString(), userString, 10, TimeUnit.MINUTES)
                user
            } else {
                objectMapper!!.readValue(userString, User::class.java)
            }
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    fun getUserList(): List<User> = userMapper!!.selectList(null)
}
