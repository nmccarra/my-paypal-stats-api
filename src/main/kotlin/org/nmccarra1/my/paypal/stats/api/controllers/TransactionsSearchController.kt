package org.nmccarra1.my.paypal.stats.api.controllers

import org.nmccarra1.my.paypal.stats.api.models.TransactionsRequestWithAccessToken
import org.nmccarra1.my.paypal.stats.api.services.PaypalAPIClientService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/paypal/transactions-search")
class TransactionsSearchController(val paypalAPIClientService: PaypalAPIClientService) {

    // TODO: Add Error handling Exception responses

    @PostMapping
    fun retrieveTransactions(@RequestBody transactionsRequestWithAccessToken : TransactionsRequestWithAccessToken) =
        paypalAPIClientService.getTransactionsWithAccessToken(transactionsRequestWithAccessToken)
}