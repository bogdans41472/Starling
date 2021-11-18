package com.bogtech.network

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.network.ApiManager
import com.bogtech.network.savings.model.*
import io.reactivex.Single
import kotlin.math.ceil

class SdkImpl : Sdk {

    @VisibleForTesting
    val apiManager: ApiManager = ApiManager()

    fun initializeWithContext(context: Context) {
        // Currently unused, but may become useful later on when more functionality is added
        // This method will get called prior to host application's App.class,
        // so we can setup prerequisites for the correct functioning of this library
    }

    override fun getAccountsList(): Single<AccountsList> {
        return apiManager.getAccountsDao()
            .getAccountsList()
    }

    private fun getMainAccount(): Single<Account> {
        return getAccountsList()
            .map(::getMainAccount)
    }

    override fun getRoundUpForLastWeek(): Single<Amount> {
        return getMainAccount()
            .flatMap { mainAccount ->
                apiManager.getFeedDao().getChangesSinceItems(
                    mainAccount.accountUid,
                    mainAccount.defaultCategory,
                    "2021-11-07T12:34:56.000Z"
                )
            }
            .flatMap { activityFeed ->
                calculateRoundupTotal(activityFeed)
            }
    }

    private fun calculateRoundupTotal(activityFeed: FeedItemList): Single<Amount> {
        var roundUpAmountTotal = 0.0
        for (activity in activityFeed.feedItems) {
            // I'm not sure if this is correct.
            // Are we expecting to account for decimal points?
            // minorUnits are supposed to be pence?
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

    override fun addMoneyToSavings(
        savingsGoalUid: String,
        transferUid: String,
        amount: SavingsGoalsAmount
    ): Single<TransferResponse> {
        return getMainAccount().flatMap { account ->
            apiManager.getSavingsDao().addMoneyToSavingsGoals(
                account.accountUid,
                savingsGoalUid,
                transferUid,
                amount
            )
        }
    }

    override fun createSavingsGoal(savingsGoals: SavingsGoals): Single<SavingsGoalResponse> {
        return getMainAccount().flatMap { account ->
            apiManager.getSavingsDao()
                .createSavingsGoals(account.accountUid, savingsGoals)
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