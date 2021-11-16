package com.bogtech.network

import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.model.subtypes.Amount
import io.reactivex.Single
import io.reactivex.annotations.NonNull

interface Sdk {

    /**
     * Returns a Single that contains a list of all accounts associated with user.
     */
    fun getAccountsList(): Single<AccountsList>

    /**
     * Returns list of all transactions executed on the account in the given timeframe
     */
    fun getTransactionFeed(
        accountUid: String,
        category: String,
        minTimestamp: String,
        maxTimestamp: String
    ) : Single<FeedItemList>

    fun getRoundUpBetweenTimestamps(
        minTimestamp: String,
        maxTimestamp: String
    ) : Single<Amount>

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