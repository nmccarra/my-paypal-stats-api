package org.nmccarra1.my.paypal.stats.api

import org.nmccarra1.my.paypal.stats.api.models.AccessTokenCredentials
import org.nmccarra1.my.paypal.stats.api.models.AccessTokenResponse
import org.nmccarra1.my.paypal.stats.api.services.Configuration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.zalando.logbook.BodyFilter
import org.zalando.logbook.json.JsonBodyFilters.replaceJsonStringProperty

@SpringBootApplication
@EnableConfigurationProperties(Configuration::class)
class AppKt {

    val excludeFields = (AccessTokenCredentials::class.java.declaredFields + AccessTokenResponse::class.java.declaredFields)
        .map { it.name }.toSet()

    @Bean
    fun bodyFilter(): BodyFilter? {
        return replaceJsonStringProperty(excludeFields, "*")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AppKt::class.java, *args)
        }
    }
}
