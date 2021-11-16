package com.bogtech.network.savings.model

data class SavingsGoals(
    val name: String,
    val currency: String,
    val target: SavingsTarget,
    val base64EncodedPhoto: String,
)
