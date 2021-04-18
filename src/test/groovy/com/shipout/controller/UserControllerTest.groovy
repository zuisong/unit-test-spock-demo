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


    ObjectMapper mapper = new ObjectMapper()

    def "查询用户列表测试"() {
        def userController = new UserController(userService: userService)
        MockMvc mockMvc = standaloneSetup(userController).build()
        when: "calling web service and get a response"

        def result = mockMvc
                .perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8.toString())
                )
                .andDo(MockMvcResultHandlers.log())

        then: "expect that a valid response occurs ..."
        def response = result

                .andReturn()
                .response
        response.getStatus() == 200
        1 * userService.getUserList() >> [new User()]
        result.andExpect(jsonPath("\$.code").value(0))
        result.andExpect(jsonPath("\$.data").hasJsonPath())
        result.andExpect(jsonPath("\$.data..id").hasJsonPath())

        response.getContentAsString(StandardCharsets.UTF_8) == '{"code":0,"data":[{"id":null,"name":null}],"msg":"success"}'
    }

    def "插入用户测试"() {
        when: "MockMvcWebTestClient 测试"


        def userController = new UserController(userService: userService)
        WebTestClient client = MockMvcWebTestClient
                .bindToController(userController)
                .build();

        client.post()
                .uri("/user/addUser")
                .bodyValue(new Gson().newBuilder().serializeNulls().create().toJson(new User()))
                .headers {
                    it.setContentType(MediaType.APPLICATION_JSON)
                }
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath('$.code').isEqualTo(0)
                .jsonPath('$.data').isEmpty()
                .jsonPath('$.msg').isNotEmpty()


        then:
        noExceptionThrown()
    }
}
