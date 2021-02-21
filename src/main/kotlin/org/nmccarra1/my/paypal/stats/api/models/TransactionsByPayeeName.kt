package org.nmccarra1.my.paypal.stats.api.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date

data class TransactionsByPayeeName(
    val accessToken: String,
    val payeeName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val startDate: Date,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val endDate: Date
) {
    fun toTransactionsRequestWithAccessToken() =
        TransactionsRequestWithAccessToken(
            accessToken = this.accessToken,
            startDate = this.startDate,
            endDate = this.endDate
        )
}
