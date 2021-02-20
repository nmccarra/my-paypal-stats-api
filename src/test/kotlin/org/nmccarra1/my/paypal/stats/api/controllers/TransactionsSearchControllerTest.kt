package org.nmccarra1.my.paypal.stats.api.controllers

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.integration.ClientAndServer
import org.nmccarra1.my.paypal.stats.api.controllers.MockTransactionSearchRequests.transactionSearchSuccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.hamcrest.CoreMatchers.equalTo

@SpringBootTest
@AutoConfigureMockMvc
internal class TransactionsSearchControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    val clientAndServer = ClientAndServer(1080)

    @BeforeEach
    fun setup() {
        transactionSearchSuccess(clientAndServer)
    }

    @AfterEach
    fun tearDown() {
        clientAndServer.stop()
    }

    @Test
    fun `should return SUCCESS when the transaction search response is received and parsed correctly`() {
        val request = "{\"accessToken\":\"success_access_token\",\"startDate\":\"2021-02-01\",\"endDate\":\"2021-02-13\"}"

        mockMvc.post("/api/paypal/transactions-search") {
            contentType = MediaType.APPLICATION_JSON
            content = request
        }
            .andDo { println() }
            .andExpect {
                status { isOk }
                content { contentType(MediaType.APPLICATION_JSON) }

                jsonPath("$[0].transactionId", equalTo("MDFEKWLQFE3323"))
                jsonPath("$[0].transactionDate", equalTo("2021-02-02T12:21:14+0000"))
                jsonPath("$[0].transactionType", equalTo("PAP"))
                jsonPath("$[0].transactionEventCode", equalTo("T0003"))
                jsonPath("$[0].payerName", equalTo("Spotify Finance Limited"))
                jsonPath("$[0].items", equalTo(listOf<String>()))
                jsonPath("$[0].transactionAmount", equalTo("-9.83"))

                jsonPath("$[1].transactionId", equalTo("0DEKFLRKCKRN555"))
                jsonPath("$[1].transactionDate", equalTo("2021-02-06T12:21:14+0000"))
                jsonPath("$[1].transactionType", equalTo("TXN"))
                jsonPath("$[1].transactionEventCode", equalTo("T0300"))
                jsonPath("$[1].payerName", equalTo(null))
                jsonPath("$[1].items", equalTo(listOf<String>()))
                jsonPath("$[1].transactionAmount", equalTo("9.83"))
            }
    }

    @Test
    fun `should return BAD REQUEST when start_date or end_date is not in the correct date format`() {
        val request = "{\"accessToken\":\"success_access_token\",\"startDate\":\"2021-02-\",\"endDate\":\"2021-02-13\"}"

        val response = mockMvc.post("/api/paypal/transactions-search") {
            contentType = MediaType.APPLICATION_JSON
            content = request
        }

        response
            .andDo { println() }
            .andExpect {
                status { isBadRequest }
                content { contentType(MediaType.APPLICATION_JSON) }

                jsonPath("$.additionalInfo", equalTo("Cannot deserialize value of type `java.util.Date` from String \"2021-02-\": expected format \"yyyy-MM-dd\""))
            }
    }
}
