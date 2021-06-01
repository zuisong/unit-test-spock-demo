package com.shipout.controller

import com.shipout.entity.*
import com.shipout.service.*
import groovy.json.*
import io.restassured.http.*
import io.restassured.module.mockmvc.*
import org.springframework.http.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.*
import spock.lang.*

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

class UserControllerTest extends Specification {
    UserService userService = Mock(UserService)
    def userController = new UserController(userService: userService)
    MockMvc mockMvc = standaloneSetup(userController).build()

    def setup() {
        RestAssuredMockMvc.reset()
        RestAssuredMockMvc.resultHandlers(MockMvcResultHandlers.log())
        RestAssuredMockMvc.mockMvc(mockMvc)
    }

    def "查询用户列表测试"() {
        given: "初始化环境"
        when: "calling web service and get a response"

        def response = RestAssuredMockMvc
                .get("/user/list")

        then:

        response.statusCode == 200
        verifyAll response.jsonPath(), {
            get('code') == 0
            get('data') != null
            get('data[0].id') == 1
        }

        def jsonSlurper = new JsonSlurper()

        response.getBody().jsonPath().get() == jsonSlurper.parseText('''{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": null
    }
  ],
  "msg": "success"
}''')

        then: "expect that a valid response occurs ..."


        1 * userService.getUserList() >> [new User(id: 1)]


    }

    def "插入用户测试"() {
        given: "初始化环境"


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
