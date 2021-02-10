package org.nmccarra1.my.paypal.stats.api.models

data class UnsuccessfulMessage(val error: String)

object UnsuccessfulMessages {
    val UNAUTHORIZED_ACCESS_TOKEN = UnsuccessfulMessage("Invalid credentials provided. Please try again.")
}