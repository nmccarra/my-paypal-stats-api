package org.nmccarra1.my.paypal.stats.api.models

data class TransactionsRequestWithAccessToken (
    val accessToken: String,
    val startDate: String,
    val endDate: String?
) {
    fun parameterPairList() : List<Pair<String, String>> {
        return listOf("fields" to "all",
        "start_date" to "${startDate}T00:00:00-00:00",
        "end_date" to "${endDate}T23:59:59-00:00")
    }
}