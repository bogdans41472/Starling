package com.bogtech.network.account.service

import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.network.ApiManager.Companion.token
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface RemoteAccountApi {

    // Get Call to retrieve accounts information
    @GET("accounts")
    @Headers("Authorization: Bearer $token")
    fun getAccountLists(): Single<AccountsList>
}