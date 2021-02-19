package org.nmccarra1.my.paypal.stats.api.controllers

import org.nmccarra1.my.paypal.stats.api.services.Configuration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/info")
class InfoController(val configuration: Configuration) {

    @GetMapping
    fun info() = configuration
}