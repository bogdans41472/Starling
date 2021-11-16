package com.bogtech.network.savings.model

data class SavingsGoalResponse(
    val savingsGoalUid: String,
    val success: Boolean,
    val errors: List<Error>
)
