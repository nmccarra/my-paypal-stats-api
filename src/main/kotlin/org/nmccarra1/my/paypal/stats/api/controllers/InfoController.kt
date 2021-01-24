package org.nmccarra1.my.paypal.stats.api.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class InfoController {
    @GetMapping("/info")
    fun info() = "My Paypal Stats API v0.1"
}