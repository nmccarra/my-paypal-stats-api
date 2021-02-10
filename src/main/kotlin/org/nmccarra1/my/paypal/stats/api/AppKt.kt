package org.nmccarra1.my.paypal.stats.api

import mu.KLogger
import mu.KotlinLogging
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AppKt {

companion object {
    @JvmStatic
    fun main(args: Array<String>){
        SpringApplication.run(AppKt::class.java, *args)
        }
    val logger: KLogger = KotlinLogging.logger {}
    }
}