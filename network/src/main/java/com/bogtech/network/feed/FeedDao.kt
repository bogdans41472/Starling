package com.bogtech.network.feed

import com.bogtech.network.exception.InternalError
import com.bogtech.network.exception.InternalException
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.service.RemoteFeedApi
import io.reactivex.Single
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException

class FeedDao(
    private val retrofit: Retrofit
) {

    fun getRemoteFeed(
        accountUid: String,
        categoryUid: String,
        minTimestamp: String,
        maxTimestamp: String
    ): Single<FeedItemList> {
        return retrofit.create(RemoteFeedApi::class.java)
            .getFeedItems(accountUid, categoryUid, minTimestamp, maxTimestamp)
            .onErrorResumeNext(this::handleError)
    }

    fun getRemoteFeedApi(): RemoteFeedApi {
        return retrofit.create(RemoteFeedApi::class.java)
    }

    private fun handleError(throwable: Throwable): Single<FeedItemList> {
        if (throwable is IOException || throwable is UnknownHostException) {
            return Single.error(
                InternalException(
                    InternalError.NETWORK_ERROR,
                    "There was a problem with your connection, please check your connection"
                )
            )
        }

        return Single.error(throwable)
    }
}