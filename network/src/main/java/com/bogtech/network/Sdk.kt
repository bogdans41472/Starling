package com.bogtech.network

import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import io.reactivex.Single
import io.reactivex.annotations.NonNull

interface Sdk {

    fun getAccountsList(): Single<AccountsList>

    fun getTransactionFeed(
        @NonNull accountUid: String,
        @NonNull category: String
    )
            : Single<FeedItemList>

    fun getSavingsGoals(

    )

    companion object {
        private var testInstance: Sdk? = null

        fun getInstance(): Sdk {
            return if (testInstance != null) ({
                testInstance = SdkImpl.getInstance()
                testInstance
            }) as SdkImpl
            else {
                SdkImpl.getInstance()
            }
        }

        internal fun setInstance(sdk: Sdk) {
            testInstance = sdk
        }
    }
}