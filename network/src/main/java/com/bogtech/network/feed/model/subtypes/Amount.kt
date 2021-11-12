package com.bogtech.network.feed.model.subtypes

data class Amount (
    val currency: String,
    val minorUnits: Int,
)