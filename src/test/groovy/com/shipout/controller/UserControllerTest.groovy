package com.shipout.controller

import com.fasterxml.jackson.databind.*
import com.google.gson.*
import com.shipout.entity.*
import com.shipout.service.*
import org.springframework.http.*
import org.springframework.test.web.reactive.server.*
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.client.*
import org.springframework.test.web.servlet.result.*
import spock.lang.*

import java.nio.charset.*

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

class UserControllerTest extends Specification {
    UserService userService = Mock(UserService)
    def userController = new UserController(userService: userService)

    def "查询用户列表测试"() {
        given:"初始化环境"
        MockMvc mockMvc = standaloneSetup(userController).build()
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
        WebTestClient client = MockMvcWebTestClient
                .bindToController(userController)
                .build();
        when: "MockMvcWebTestClient 测试"

        client.post()
                .uri("/user/addUser")
                .bodyValue(new Gson().newBuilder().serializeNulls().create().toJson(new User()))
                .headers {
                    it.setContentType(MediaType.APPLICATION_JSON)
                }
                .exchange()
                .expectHeader()
                .with {
                    it.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                }
                .expectStatus()
                .with {
                    it.is2xxSuccessful()
                }
                .expectBody()
                .with {
                    it.consumeWith { println(it) }
                    it.jsonPath('$.code').isEqualTo(0)
                    it.jsonPath('$.data').isEmpty()
                    it.jsonPath('$.msg').isNotEmpty()
                }
                .returnResult()


        then:
        noExceptionThrown()
    }
}
