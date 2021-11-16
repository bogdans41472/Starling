package com.bogtech.network

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.network.ApiManager
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Single
import kotlin.math.ceil

class SdkImpl : Sdk {

    @VisibleForTesting
    val apiManager: ApiManager = ApiManager()

    @VisibleForTesting
    val rxSchedulers: DefaultRxSchedulers = DefaultRxSchedulers()

    fun initializeWithContext(context: Context) {
        // Setup prequisites
    }

    override fun getAccountsList(): Single<AccountsList> {
        return apiManager.getAccountsDao()
            .getAccountsList()
            .observeOn(rxSchedulers.ui())
            .subscribeOn(rxSchedulers.io())
    }

    override fun getTransactionFeed(
        accountUid: String,
        category: String,
        minTimestamp: String,
        maxTimestamp: String
    ): Single<FeedItemList> {
        return apiManager.getFeedDao()
            .getRemoteFeed(accountUid, category, minTimestamp, maxTimestamp)
            .observeOn(rxSchedulers.ui())
            .subscribeOn(rxSchedulers.io())
    }

    override fun getRoundUpBetweenTimestamps(
        minTimestamp: String,
        maxTimestamp: String
    ) : Single<Amount> {
        return getAccountsList()
            .map(::getMainAccount)
            .flatMap { mainAccount ->
                getTransactionFeed(mainAccount.accountUid,
                    mainAccount.defaultCategory,
                    minTimestamp,
                    maxTimestamp) }
            .flatMap { activityFeed ->
                var roundUpAmountTotal = 0.0
                for (activity in activityFeed.feedItems) {
                    val minorUnits = activity.amount.minorUnits
                    val roundedAmount = ceil(minorUnits)
                    roundUpAmountTotal = roundedAmount - minorUnits
                }
                Single.just(Amount(activityFeed.feedItems[0].amount.currency,
                    roundUpAmountTotal))
            }
    }

    private fun getMainAccount(accountsList: AccountsList): Account {
        return accountsList.accounts[0]
    }

    override fun getSavingsGoals() {
        TODO("Not yet implemented")
    }

    companion object {
        fun getInstance(): SdkImpl {
            return Holder.INSTANCE
        }

        private object Holder {
            val INSTANCE by lazy { SdkImpl() }
        }
    }
}