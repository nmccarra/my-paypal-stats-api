package org.nmccarra1.my.paypal.stats.api.models

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.ResponseDeserializable
import java.util.Date

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TransactionsSearchResponse(
    val transactionDetails: List<PaypalTransactionSubset?>,
    val totalItems: Int
) {
    fun toPaypalTransactionSearchParsedResponse(): List<TransactionsSearchParsedResponse?> {
        return this.transactionDetails.mapNotNull {
            when (val transaction = it) {
                is PaypalTransactionSubset -> {
                    TransactionsSearchParsedResponse(
                        transactionId = transaction.transactionInfo.transactionId,
                        transactionDate = transaction.transactionInfo.transactionInitiationDate,
                        transactionType = transaction.transactionInfo.paypalReferenceIdType,
                        transactionEventCode = transaction.transactionInfo.transactionEventCode,
                        payerName = transaction.payerInfo.payerName?.alternateFullName,
                        items = transaction.cartInfo.itemsList(),
                        transactionAmount = transaction.transactionInfo.transactionAmount.value

                    )
                }
                else -> null
            }
        }
    }
}

data class TransactionsSearchParsedResponse(
    val transactionId: String,
    val transactionDate: String,
    val transactionType: String?,
    val transactionEventCode: String?,
    val payerName: String?,
    val items: List<String>,
    val transactionAmount: String
)

data class TransactionsSearchByPayeeNameResponse(
    val payeeName: String,
    val transactionsCount: Int,
    val transactionSums: TransactionSums,
    val startDate: Date,
    val endDate: Date,
    val transactions: List<TransactionsSearchParsedResponse?>
)

data class TransactionSums(
    val prePaid: Double = 0.0,
    val standard: Double = 0.0
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PaypalTransactionSubset(
    val transactionInfo: TransactionInfo,
    val payerInfo: PayerInfo,
    val cartInfo: CartInfo
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TransactionInfo(
    val transactionId: String,
    val transactionInitiationDate: String,
    val paypalReferenceIdType: String?,
    val transactionEventCode: String?,
    val transactionAmount: TransactionAmount,
    val transactionStatus: String
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class TransactionAmount(
    val currencyCode: String,
    val value: String
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PayerInfo(
    val payerName: PayerName?
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class PayerName(
    val alternateFullName: String?
)

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class CartInfo(
    val itemDetails: List<ItemInfo?>
) {
    fun itemsList(): List<String> {
        return itemDetails.mapNotNull { it?.itemName }
    }
}

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class ItemInfo(
    val itemName: String?
)

object TransactionsSearchResponseDeserializer : ResponseDeserializable<TransactionsSearchResponse> {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    override fun deserialize(content: String): TransactionsSearchResponse =
        mapper.readValue<TransactionsSearchResponse>(content, TransactionsSearchResponse::class.java)
}
