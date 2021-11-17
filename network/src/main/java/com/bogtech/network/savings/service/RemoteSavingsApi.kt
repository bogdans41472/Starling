package com.bogtech.network.savings.service

import com.bogtech.network.network.ApiManager
import com.bogtech.network.savings.model.*
import io.reactivex.Single
import retrofit2.http.*

interface RemoteSavingsApi {

    @PUT("account/{accountUid}/savings-goals")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun createSavingsGoals(
        @Path("accountUid") accountUid: String,
        @Body body: SavingsGoals
    ): Single<SavingsGoalResponse>

    @PUT("account/{accountUid}/savings-goals/{savingsGoalsUid}/add-money/{transferUid}")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun addMoneyToSavingsGoals(
        @Path("accountUid") accountUid: String,
        @Path("savingsGoalsUid") savingsGoalsUid: String,
        @Path("transferUid") transferUid: String,
        @Body body: SavingsGoalsAmount
    ): Single<TransferResponse>

    @GET("account/{accountUid}/savings-goals")
    @Headers("Authorization: Bearer ${ApiManager.token}")
    fun getSavingsGoals(
        @Path("accountUid") accountUid: String,
    ): Single<SavingsGoalsList>
}