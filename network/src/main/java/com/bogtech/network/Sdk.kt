package com.bogtech.network

import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.savings.model.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.annotations.NonNull

interface Sdk {

    /**
     * Returns a list of all accounts associated with user.
     */
    fun getAccountsList(): Single<AccountsList>

    /**
     * Retrieves Amount of last week transactions.
     */
    fun getRoundUpForLastWeek(): Single<Amount>

    /**
     * Create a new savings goal
     */
    fun createSavingsGoal(savingsGoals: SavingsGoals): Single<SavingsGoalResponse>

    /**
     * Adds money to savings account
     */
    fun addMoneyToSavings(
        savingsGoalUid: String,
        transferUid: String,
        amount: SavingsGoalsAmount
    ): Single<TransferResponse>

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