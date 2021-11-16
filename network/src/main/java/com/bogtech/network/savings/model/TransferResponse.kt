package com.bogtech.network.savings.model

data class TransferResponse(
    val transferUid: String,
    val success: Boolean,
    val errors: List<Error>
)
