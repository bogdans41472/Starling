package com.bogtech.network.feed.service

import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.network.ApiManager
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteFeedApi {

    @GET("feed/account/{accountUid}/category/{categoryUid}/transactions-between")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun getFeedItems(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("minTransactionTimestamp") minTransactionTimestamp: String,
        @Query("maxTransactionTimestamp") maxTransactionTimestamp: String,
    ): Single<FeedItemList>

    @GET("feed/account/{accountUid}/category/{categoryUid}")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun getChangesSinceItems(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("changesSince") timestamp: String
    ): Single<FeedItemList>
}