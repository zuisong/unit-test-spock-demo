package com.shipout.controller


import com.shipout.entity.*
import com.shipout.service.*
import io.restassured.http.*
import io.restassured.module.mockmvc.*
import org.springframework.http.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.*
import spock.lang.*

import java.nio.charset.*

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

class UserControllerTest extends Specification {
    UserService userService = Mock(UserService)
    def userController = new UserController(userService: userService)
    MockMvc mockMvc = standaloneSetup(userController).build()

    def "查询用户列表测试"() {
        given: "初始化环境"
        when: "calling web service and get a response"

        mockMvc
                .perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                )
                .andDo(MockMvcResultHandlers.log())
                .with { it ->
                    [status().is2xxSuccessful(),
                     jsonPath('$.code').value(0),
                     jsonPath('$.data').hasJsonPath(),
                     jsonPath('$.data..id').hasJsonPath(),
                     content().json('{"code":0,"data":[{"id":null,"name":null}],"msg":"success"}'),
                    ].forEach it::andExpect
                }

        then: "expect that a valid response occurs ..."


        1 * userService.getUserList() >> [new User()]


    }

    def "插入用户测试"() {
        given: "初始化环境"

        RestAssuredMockMvc.reset()
        RestAssuredMockMvc.resultHandlers(MockMvcResultHandlers.log())
        RestAssuredMockMvc.mockMvc(mockMvc)

        when: "MockMvcWebTestClient 测试"

        def result = RestAssuredMockMvc.given()
                .body(new User())
                .contentType(ContentType.JSON)
                .post("/user/addUser")

        then:
        result.contentType == MediaType.APPLICATION_JSON_VALUE

        result.statusCode() == 200

        result.jsonPath().with {
            it.getInt('code') == 0
            it.getString('data') == null
            !it.getString('msg').isBlank()
        }


        then:
        noExceptionThrown()
    }
}
