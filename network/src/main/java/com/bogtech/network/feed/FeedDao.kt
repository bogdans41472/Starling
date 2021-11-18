package com.bogtech.network.feed

import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.service.RemoteFeedApi
import com.bogtech.network.network.BaseDao
import io.reactivex.Single
import retrofit2.Retrofit

/**
 * FeedDao is responsible for making /feed calls via RemoteFeedApi retrofit.
 */
class FeedDao(
    private val retrofit: Retrofit
) : BaseDao() {

    fun getChangesSinceItems(
        accountUid: String,
        defaultCategory: String,
        timestamp: String
    ): Single<FeedItemList> {
        return getDefaultSingle(retrofit.create(RemoteFeedApi::class.java)
            .getChangesSinceItems(accountUid, defaultCategory, timestamp))
    }
}