package org.nmccarra1.my.paypal.stats.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.*
import org.mockserver.integration.ClientAndServer
import org.nmccarra1.my.paypal.stats.api.controllers.MockOauth2TokenRequests.oauth2TokenSuccess
import org.nmccarra1.my.paypal.stats.api.controllers.MockOauth2TokenRequests.oauth2TokenUnauthorised
import org.nmccarra1.my.paypal.stats.api.models.AccessTokenCredentials
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class AccessTokenControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    val clientAndServer = ClientAndServer(1080)

        @BeforeEach
        fun setUp() {
            oauth2TokenSuccess(clientAndServer)
            oauth2TokenUnauthorised(clientAndServer)

        }

        @AfterEach
        fun tearDown() {
            clientAndServer.stop()
        }

    @Test
    fun `should return SUCESS when an access token has been retrieved`() {
        val credentials = AccessTokenCredentials("clientId", "clientPassword")

        mockMvc.post("/api/token") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(credentials)
        }
            .andDo { println() }
            .andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.accessToken", equalTo("TOKEN"))
            }
    }

    @Test
    fun `should return UNAUTHORISED when Paypal API responded with UNAUTHORISED`() {
        val credentials = AccessTokenCredentials("unauthUser", "unauthPassword")

        mockMvc.post("/api/token") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(credentials)
        }
            .andDo { println() }
            .andExpect {
                status { isUnauthorized }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }
}