package com.bogtech.network.savings.service

import com.bogtech.network.network.ApiManager
import com.bogtech.network.savings.model.SavingsGoalResponse
import com.bogtech.network.savings.model.SavingsGoals
import com.bogtech.network.savings.model.SavingsGoalsAmount
import com.bogtech.network.savings.model.TransferResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

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
}