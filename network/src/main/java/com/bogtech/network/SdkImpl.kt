package com.bogtech.network

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.network.ApiManager
import com.bogtech.network.savings.model.*
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import kotlin.math.ceil
import kotlin.math.roundToLong

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

    private fun getMainAccount(): Single<Account> {
        return getAccountsList()
            .map(::getMainAccount)
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

    override fun getRoundUpForLastWeek(): Single<Amount> {
        return getMainAccount()
            .flatMap { mainAccount ->
                apiManager.getFeedDao().getRemoteFeedApi().getChangesSinceItems(
                    mainAccount.accountUid,
                    mainAccount.defaultCategory,
                    "2021-11-07T12:34:56.000Z"
                )
                    .subscribeOn(rxSchedulers.io())
                    .observeOn(rxSchedulers.ui())
            }
            .flatMap { activityFeed ->
                calculateRoundupTotal(activityFeed)
            }
    }

    private fun calculateRoundupTotal(activityFeed: FeedItemList): Single<Amount> {
        var roundUpAmountTotal = 0.0
        for (activity in activityFeed.feedItems) {
            val minorUnits = activity.amount.minorUnits.toDouble() / 100
            val roundedAmount = ceil(minorUnits)
            roundUpAmountTotal += roundedAmount - minorUnits
        }
        return Single.just(
            Amount(
                activityFeed.feedItems[0].amount.currency,
                roundUpAmountTotal.toLong()
            )
        )
    }

    override fun getSavingsGoals(): Single<SavingsGoalsList> {
        return getMainAccount().flatMap { account ->
            apiManager.getSavingsDao().getRemoteSavingsApi()
                .getSavingsGoals(account.accountUid)
                .subscribeOn(rxSchedulers.io())
        }
    }

    override fun addMoneyToSavings(
        savingsGoalUid: String,
        transferUid: String,
        amount: SavingsGoalsAmount
    ): Single<TransferResponse> {
        return getMainAccount().flatMap { account ->
            apiManager.getSavingsDao().getRemoteSavingsApi()
                .addMoneyToSavingsGoals(
                    account.accountUid,
                    savingsGoalUid,
                    transferUid,
                    amount
                ).subscribeOn(rxSchedulers.io())
        }
    }

    override fun createSavingsGoal(savingsGoals: SavingsGoals): Single<SavingsGoalResponse> {
        return getMainAccount().flatMap { account ->
            apiManager.getSavingsDao().getRemoteSavingsApi()
                .createSavingsGoals(account.accountUid, savingsGoals)
                .subscribeOn(rxSchedulers.io())
        }
    }

    private fun getMainAccount(accountsList: AccountsList): Account {
        return accountsList.accounts[0]
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