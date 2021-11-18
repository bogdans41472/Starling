package com.bogtech.network.savings

import com.bogtech.network.network.BaseDao
import com.bogtech.network.savings.model.*
import com.bogtech.network.savings.service.RemoteSavingsApi
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Single
import retrofit2.Retrofit

/**
 * Savings is responsible for making /savings calls via RemoteSavingsApi retrofit.
 */
class SavingsDao(
    private val retrofit: Retrofit,
) : BaseDao() {

    fun addMoneyToSavingsGoals(
        accountUid: String,
        savingsGoalUid: String,
        transferUid: String,
        amount: SavingsGoalsAmount
    ): Single<TransferResponse> {
        return getDefaultSingle(retrofit.create(RemoteSavingsApi::class.java)
            .addMoneyToSavingsGoals(accountUid, savingsGoalUid, transferUid, amount))
    }

    fun createSavingsGoals(
        accountUid: String,
        savingsGoals: SavingsGoals
    ): Single<SavingsGoalResponse> {
        return getDefaultSingle(retrofit.create(RemoteSavingsApi::class.java)
            .createSavingsGoals(accountUid, savingsGoals))
    }
}