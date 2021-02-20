package org.nmccarra1.my.paypal.stats.api.controllers

import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import java.util.concurrent.TimeUnit

object MockOauth2TokenRequests {

    fun oauth2TokenSuccess(clientAndServer: ClientAndServer, tokenValue: String = "TOKEN") {
        clientAndServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("/v1/oauth2/token")
                .withHeader("Content-Type", "application/x-www-form-urlencoded")
                .withHeader("Authorization", "Basic Y2xpZW50SWQ6Y2xpZW50UGFzc3dvcmQ=")
                .withBody("grant_type=client_credentials")
        ).respond(
            HttpResponse.response()
                .withStatusCode(200)
                .withHeader("\"Content-type\", \"application/json\"")
                .withBody("{\"access_token\": \"$tokenValue\"}")
                .withDelay(TimeUnit.NANOSECONDS, 1)
        )
    }

    fun oauth2TokenUnauthorised(clientAndServer: ClientAndServer) {
        clientAndServer.`when`(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("/v1/oauth2/token")
                .withHeader("Content-Type", "application/x-www-form-urlencoded")
                .withHeader("Authorization", "Basic dW5hdXRoVXNlcjp1bmF1dGhQYXNzd29yZA==")
                .withBody("grant_type=client_credentials")
        ).respond(
            HttpResponse.response()
                .withStatusCode(401)
                .withHeader("\"Content-type\", \"application/json\"")
                .withBody("{\"access_token\": \"null\"}")
                .withDelay(TimeUnit.NANOSECONDS, 1)
        )
    }
}

object MockTransactionSearchRequests {

    fun transactionSearchSuccess(clientAndServer: ClientAndServer) {
        clientAndServer.`when`(
            HttpRequest.request()
                .withMethod("GET")
                .withPath("/v1/reporting/transactions")
                .withHeader("Content-Type", "application/json")
                .withHeader("Authorization", "Bearer success_access_token")
                .withQueryStringParameters(
                    Parameter("fields", "all"),
                    Parameter("start_date", "2021-02-01T00:00:00-00:00"),
                    Parameter("end_date", "2021-02-13T23:59:59-00:00")
                )
        ).respond(
            HttpResponse.response()
                .withStatusCode(200)
                .withHeader("\"Content-type\", \"application/json\"")
                .withBody(MockTransactionSearchRequests::class.java.getResource("/transaction_search_response.json").readText())
                .withDelay(TimeUnit.NANOSECONDS, 1)
        )
    }
}
