package org.nmccarra1.my.paypal.stats.api.models

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.Date
import java.text.SimpleDateFormat

data class TransactionsRequestWithAccessToken(
    val accessToken: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val startDate: Date,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    val endDate: Date
) {
    fun parameterPairList(): List<Pair<String, String>> {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        return listOf(
            "fields" to "all",
            "start_date" to "${formatter.format(startDate)}T00:00:00-00:00",
            "end_date" to "${formatter.format(endDate)}T23:59:59-00:00"
        )
    }
}
