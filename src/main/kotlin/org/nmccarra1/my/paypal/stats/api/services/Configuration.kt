package org.nmccarra1.my.paypal.stats.api.services

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app")
data class Configuration(
    val version: String = System.getenv("APP_VERSION") ?: "0.0.0",
    val paypal: Paypal) {
    data class Paypal(
        val url: String,
        val transactionsPath: String,
        val tokenPath: String
    )
}