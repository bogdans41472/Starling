package com.bogtech.network.feed.service

import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.network.ApiManager
import io.reactivex.Single
import retrofit2.http.*

interface RemoteFeedApi {

    @GET("feed/account/{accountUid}/category/{categoryUid}")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun getAccountLists(
        @Path("accountUid") accountUid: String,
        @Path("categoryUid") categoryUid: String,
        @Query("changesSince") timestamp: String = "2021-01-01T12:34:56.000Z"
    ): Single<FeedItemList>
}