package org.nmccarra1.my.paypal.stats.api.services

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import org.nmccarra1.my.paypal.stats.api.models.AccessTokenCredentials
import org.nmccarra1.my.paypal.stats.api.models.AccessTokenResponse
import org.nmccarra1.my.paypal.stats.api.models.Oauth2TokenResponseDeserializer
import org.nmccarra1.my.paypal.stats.api.models.TransactionSums
import org.nmccarra1.my.paypal.stats.api.models.TransactionsByPayeeName
import org.nmccarra1.my.paypal.stats.api.models.TransactionsRequestWithAccessToken
import org.nmccarra1.my.paypal.stats.api.models.TransactionsSearchByPayeeNameResponse
import org.nmccarra1.my.paypal.stats.api.models.TransactionsSearchParsedResponse
import org.nmccarra1.my.paypal.stats.api.models.TransactionsSearchResponse
import org.nmccarra1.my.paypal.stats.api.models.TransactionsSearchResponseDeserializer
import org.nmccarra1.my.paypal.stats.api.models.UnsuccessfulMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PaypalApiClientService(
    @Autowired private val configuration: Configuration
) {
    private val tokenURL = "${configuration.paypal.url}/${configuration.paypal.tokenPath}"
    private val transactionsURL = "${configuration.paypal.url}/${configuration.paypal.transactionsPath}"

    fun getAccessToken(credentials: AccessTokenCredentials): ResponseEntity<Any> {
        val (_, httpResponse, apiResult) = Fuel.post(tokenURL)
            .header(mapOf("Content-Type" to "application/x-www-form-urlencoded"))
            .authentication()
            .basic(username = credentials.clientId, password = credentials.clientPassword)
            .body("grant_type=client_credentials")
            .responseObject(Oauth2TokenResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                AccessTokenResponse(accessToken = it.accessToken)
            },
            failure = {
                UnsuccessfulMessage(error = it.response.responseMessage, additionalInfo = it.localizedMessage)
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }

    fun getTransactionsWithAccessToken(transactionsRequestWithAccessToken: TransactionsRequestWithAccessToken): ResponseEntity<Any> {
        val (_, httpResponse, apiResult) = Fuel.get(transactionsURL, transactionsRequestWithAccessToken.parameterPairList())
            .header(mapOf("Content-Type" to "application/json"))
            .authentication()
            .bearer(transactionsRequestWithAccessToken.accessToken)
            .responseObject(TransactionsSearchResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                it.toPaypalTransactionSearchParsedResponse()
            },
            failure = {
                UnsuccessfulMessage(error = it.response.responseMessage, additionalInfo = it.localizedMessage)
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }

    fun getTransactionsByPayeeName(transactionsByPayeeName: TransactionsByPayeeName): ResponseEntity<Any> {
        val (_, httpResponse, apiResult) = Fuel.get(
            transactionsURL,
            transactionsByPayeeName.toTransactionsRequestWithAccessToken().parameterPairList()
        )
            .header(mapOf("Content-Type" to "application/json"))
            .authentication()
            .bearer(transactionsByPayeeName.accessToken)
            .responseObject(TransactionsSearchResponseDeserializer)
        val payload = apiResult.fold(
            success = {
                reduceToPayeeRelatedTransactions(transactionsByPayeeName, it)
            },
            failure = {
                UnsuccessfulMessage(error = it.response.responseMessage, additionalInfo = it.localizedMessage)
            }
        )
        return ResponseEntity(payload, HttpStatus.valueOf(httpResponse.statusCode))
    }

    fun reduceToPayeeRelatedTransactions(transactionsByPayeeName: TransactionsByPayeeName, response: TransactionsSearchResponse): TransactionsSearchByPayeeNameResponse {
        val transactions = response.toPaypalTransactionSearchParsedResponse()
            .filter {
                it?.payerName?.toLowerCase()?.contains(transactionsByPayeeName.payeeName.toLowerCase())
                    ?: false
            }

        return TransactionsSearchByPayeeNameResponse(
            payeeName = transactionsByPayeeName.payeeName,
            transactionsCount = transactions.size,
            transactionSums = getTransactionSums(transactions),
            startDate = transactionsByPayeeName.startDate,
            endDate = transactionsByPayeeName.endDate,
            transactions = transactions
        )
    }

    fun getTransactionSums(transactions: List<TransactionsSearchParsedResponse?>): TransactionSums {
        return TransactionSums(
            prePaid = transactions.filter {
                PREPAID_EVENT_CODES.contains(it?.transactionEventCode)
            }
                .sumByDouble {
                    it?.transactionAmount?.toDoubleOrNull() ?: 0.0
                },
            standard = transactions.filter {
                STANDARD_EVENT_CODES.contains(it?.transactionEventCode)
            }
                .sumByDouble {
                    it?.transactionAmount?.toDoubleOrNull() ?: 0.0
                }
        )
    }

    companion object {
        val PREPAID_EVENT_CODES = listOf<String>("T0003")
        val STANDARD_EVENT_CODES = listOf<String>("T0300", "T0006", "T0700")
    }
}
