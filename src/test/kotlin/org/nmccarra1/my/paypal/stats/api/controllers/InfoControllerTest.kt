package org.nmccarra1.my.paypal.stats.api.controllers

import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.nmccarra1.my.paypal.stats.api.services.PaypalApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
internal class InfoControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @Test
    fun `should return configuration`() {
        val expectedPaypalApi = PaypalApi(
            url = "http://localhost:1080",
            transactionsPath = "v1/reporting/transactions",
            tokenPath = "v1/oauth2/token")

        mockMvc.get("/api/info")
            .andDo { println() }
            .andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.version", equalTo("0.0.0"))
                jsonPath("$.paypalApi.url", equalTo(expectedPaypalApi.url))
                jsonPath("$.paypalApi.transactionsPath", equalTo(expectedPaypalApi.transactionsPath))
                jsonPath("$.paypalApi.tokenPath", equalTo(expectedPaypalApi.tokenPath))
            }
    }
}