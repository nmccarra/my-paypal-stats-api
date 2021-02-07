package org.nmccarra1.my.paypal.stats.api.services

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.springframework.stereotype.Service

/**
 * Loads configuration from resources/application.conf
 */
@Service
class ConfigService {
    private val config: Config = ConfigFactory.load()

    val configuration = Configuration(
        version = System.getenv("APP_VERSION") ?: "0.0.0",
        paypalApi = PaypalApi(
            url = config.getString("paypal.api.url"),
            transactionsPath = config.getString("paypal.api.transactionsPath"),
            tokenPath = config.getString("paypal.api.tokenPath")
        )
    )
}

data class Configuration(
    val version: String,
    val paypalApi: PaypalApi
)

data class PaypalApi(
    val url: String,
    val transactionsPath: String,
    val tokenPath: String
)