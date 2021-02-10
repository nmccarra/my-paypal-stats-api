package org.nmccarra1.my.paypal.stats.api.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import org.nmccarra1.my.paypal.stats.api.AppKt.Companion.logger
import org.nmccarra1.my.paypal.stats.api.models.*
import org.nmccarra1.my.paypal.stats.api.models.UnsuccessfulMessages.UNAUTHORIZED_ACCESS_TOKEN
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

    fun getAccessToken(credentials: AccessTokenCredentials): ResponseEntity<AccessTokenResponse> {
        val (_, httpResponse, apiResult) = Fuel.post(tokenURL)
            .header(mapOf("Content-Type" to "application/x-www-form-urlencoded"))
            .authentication()
            .basic(username = credentials.clientId, password = credentials.clientPassword)
            .body("grant_type=client_credentials")
            .responseObject(Oauth2TokenResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                logger.info { "${httpResponse.statusCode} ${httpResponse.responseMessage} - api/token: Access token retrieved." }
                AccessTokenResponse(accessToken = it.accessToken) },
            failure = {
                logger.error { "${httpResponse.statusCode} ${httpResponse.responseMessage} - api/token: ${UNAUTHORIZED_ACCESS_TOKEN.error}" }
                throw UnauthorisedException()
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }
}