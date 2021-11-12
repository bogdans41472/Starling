package com.bogtech.network.feed.model

import com.bogtech.network.feed.model.subtypes.*

data class FeedItem(
    val feedItemUid: String,
    val categoryUid: String,
    val amount: Amount,
    val sourceAmount: Amount,
    val direction: Direction,
    val updatedAt: String,
    val transactionTime: String,
    val settlementTime: String,
    val source: SourceType,
    val status: Status,
    val counterPartyType: CounterPartyType,
    val counterPartyUid: String,
    val counterPartyName: String,
    val counterPartySubEntityUid: String,
    val counterPartySubEntityName: String,
    val counterPartySubEntityIdentifier: String,
    val counterPartySubEntitySubIdentifier: String,
    val reference: String,
    val country: String,
    val spendingCategory: SpendingCategories,
    val hasAttachment: Boolean,
    val hasReceipt: Boolean,
)