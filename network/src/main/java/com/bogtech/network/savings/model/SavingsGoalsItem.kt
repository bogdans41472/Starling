package com.bogtech.network.savings.model

import com.bogtech.network.feed.model.subtypes.Amount

data class SavingsGoalsItem(
    val savingsGoalUid: String,
    val name: String,
    val target: Amount,
    val totalSaved: Amount,
    val savedPercentage: Int,
)