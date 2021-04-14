package com.shipout.controller

import com.fasterxml.jackson.databind.*
import com.shipout.controller.*
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
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

class UserControllerTest extends Specification {
    UserService userService = Mock(UserService)
    MockMvc mockMvc = standaloneSetup(new UserController(userService: userService)).build()

    ObjectMapper mapper = new ObjectMapper()

    def "查询用户列表测试"() {
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
        result.andExpect {
            MockMvcResultMatchers.jsonPath("\$.code")

                    .value(0).match(it)
        };
        response.getContentAsString(StandardCharsets.UTF_8) == '{"code":0,"data":[{"id":null,"name":null}],"msg":"执行成功"}'
    }

    def "插入用户测试"() {
        when: "MockMvcWebTestClient 测试"

        WebTestClient client = MockMvcWebTestClient
                .bindToController(new UserController(userService: userService))
                .build();

        client.post()
                .uri("/user/addUser")

                .bodyValue(mapper.writeValueAsString(new User()))
                .headers {
                    it.setContentType(MediaType.APPLICATION_JSON)
                }

                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().jsonPath('$.code').isEqualTo(0)
                .consumeWith(System.out::println)


        then:
        noExceptionThrown()
    }
}
