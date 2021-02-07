package org.nmccarra1.my.paypal.stats.api.controllers

import org.nmccarra1.my.paypal.stats.api.services.ConfigService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/info")
class InfoController(val configService: ConfigService) {

    @GetMapping
    fun info() = configService.configuration
}