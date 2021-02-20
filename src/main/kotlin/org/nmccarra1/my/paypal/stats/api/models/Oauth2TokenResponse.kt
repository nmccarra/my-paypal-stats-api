package org.nmccarra1.my.paypal.stats.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.ResponseDeserializable

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class Oauth2TokenResponse(val accessToken: String?)

object Oauth2TokenResponseDeserializer : ResponseDeserializable<Oauth2TokenResponse> {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    override fun deserialize(content: String): Oauth2TokenResponse =
        mapper.readValue<Oauth2TokenResponse>(content, Oauth2TokenResponse::class.java)
}
