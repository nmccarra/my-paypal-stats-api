package org.nmccarra1.my.paypal.stats.api.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import org.nmccarra1.my.paypal.stats.api.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PaypalAPIClientService (
    @Autowired private val configService: ConfigService
) {
    private val config: Configuration = configService.configuration
    private val tokenURL = "${config.paypalApi.url}/${config.paypalApi.tokenPath}"
    private val transactionsURL = "${config.paypalApi.url}/${config.paypalApi.transactionsPath}"

    fun getAccessToken(credentials: AccessTokenCredentials): ResponseEntity<Any> {
        val (_, httpResponse, apiResult) = Fuel.post(tokenURL)
            .header(mapOf("Content-Type" to "application/x-www-form-urlencoded"))
            .authentication()
            .basic(username = credentials.clientId, password = credentials.clientPassword)
            .body("grant_type=client_credentials")
            .responseObject(Oauth2TokenResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                AccessTokenResponse(accessToken = it.accessToken) },
            failure = {
                UnsuccessfulMessage(error = it.response.responseMessage, additionalInfo = it.localizedMessage)
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }

    fun getTransactionsWithAccessToken(transactionsRequestWithAccessToken:
                                       TransactionsRequestWithAccessToken) : ResponseEntity<Any> {
        val (_, httpResponse, apiResult) = Fuel.get(transactionsURL, transactionsRequestWithAccessToken.parameterPairList())
            .header(mapOf("Content-Type" to "application/json"))
            .authentication()
            .bearer(transactionsRequestWithAccessToken.accessToken)
            .responseObject(TransactionsSearchResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                it.toPaypalTransactionSearchParsedResponse()
            },
            failure = { UnsuccessfulMessage(error = it.response.responseMessage, additionalInfo = it.localizedMessage)
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }
}