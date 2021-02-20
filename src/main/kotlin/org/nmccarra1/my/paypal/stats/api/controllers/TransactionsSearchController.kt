package org.nmccarra1.my.paypal.stats.api.controllers

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.nmccarra1.my.paypal.stats.api.models.UnsuccessfulMessage
import org.nmccarra1.my.paypal.stats.api.models.TransactionsRequestWithAccessToken
import org.nmccarra1.my.paypal.stats.api.services.PaypalApiClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/paypal/transactions-search")
class TransactionsSearchController(val paypalApiClientService: PaypalApiClientService) {

    @ExceptionHandler(InvalidFormatException::class)
    fun handleInvalidFormatException(e: InvalidFormatException): ResponseEntity<UnsuccessfulMessage> =
        ResponseEntity(
            UnsuccessfulMessage(
                error = "Bad Request",
                additionalInfo = e.originalMessage
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MismatchedInputException::class)
    fun handleMismatchedInputException(): ResponseEntity<UnsuccessfulMessage> =
        ResponseEntity(
            UnsuccessfulMessage(
                error = "Bad Request",
                additionalInfo = "Request did not match expected request contract"
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(JsonParseException::class)
    fun handleJsonParseException(e: JsonParseException): ResponseEntity<UnsuccessfulMessage> =
        ResponseEntity(
            UnsuccessfulMessage(
                error = "Bad Request",
                additionalInfo = "Could not successfully parse given request"
            ),
            HttpStatus.BAD_REQUEST
        )

    @PostMapping
    fun retrieveTransactions(@RequestBody transactionsRequestWithAccessToken: TransactionsRequestWithAccessToken) =
        paypalApiClientService.getTransactionsWithAccessToken(transactionsRequestWithAccessToken)
}
