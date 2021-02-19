package org.nmccarra1.my.paypal.stats.api

import mu.KLogger
import mu.KotlinLogging
import org.nmccarra1.my.paypal.stats.api.services.Configuration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties(Configuration::class)
class AppKt {

companion object {
    @JvmStatic
    fun main(args: Array<String>){
        SpringApplication.run(AppKt::class.java, *args)
        }
    val logger: KLogger = KotlinLogging.logger {}
    }
}