package com.bogtech.network.savings

import com.bogtech.network.savings.service.RemoteSavingsApi
import retrofit2.Retrofit

class SavingsDao(
    private val retrofit: Retrofit
) {

    fun getRemoteSavingsApi(): RemoteSavingsApi {
        return retrofit.create(RemoteSavingsApi::class.java)
    }
}