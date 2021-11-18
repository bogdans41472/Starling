package com.bogtech.network.network

import androidx.annotation.VisibleForTesting
import com.bogtech.network.BuildConfig
import com.bogtech.network.account.AccountsDao
import com.bogtech.network.feed.FeedDao
import com.bogtech.network.savings.SavingsDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main access point for remote or local instances of API services.
 */
open class ApiManager(
    val okHttpClient: OkHttpClient
) {

    // Get retrofit with converterFactory and OkHttpClient.
    @VisibleForTesting
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getAccountsDao(): AccountsDao {
        return AccountsDao(getRetrofit())
    }

    fun getFeedDao(): FeedDao {
        return FeedDao(getRetrofit())
    }

    fun getSavingsDao(): SavingsDao {
        return SavingsDao(getRetrofit())
    }

    companion object {
        private var BASE_URL = BuildConfig.BASE_URL
        const val token = BuildConfig.CLIENT_SECRET
    }
}