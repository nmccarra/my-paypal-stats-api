package org.nmccarra1.my.paypal.stats.api.controllers

import org.nmccarra1.my.paypal.stats.api.models.AccessTokenCredentials
import org.nmccarra1.my.paypal.stats.api.models.UnauthorisedException
import org.nmccarra1.my.paypal.stats.api.models.UnsuccessfulMessage
import org.nmccarra1.my.paypal.stats.api.models.UnsuccessfulMessages.UNAUTHORIZED_ACCESS_TOKEN
import org.nmccarra1.my.paypal.stats.api.services.PaypalAPIClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/token")
class AccessTokenController(val paypalAPIClientService: PaypalAPIClientService) {

    @ExceptionHandler(UnauthorisedException::class)
    fun handleUnauthorised(): ResponseEntity<UnsuccessfulMessage> =
        ResponseEntity(UNAUTHORIZED_ACCESS_TOKEN, HttpStatus.UNAUTHORIZED)

    @PostMapping
    fun retrieveAccessToken(@RequestBody credentials: AccessTokenCredentials) =
        paypalAPIClientService.getAccessToken(credentials)
}