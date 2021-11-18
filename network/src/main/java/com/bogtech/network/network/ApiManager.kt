package com.bogtech.network.network

import com.bogtech.network.BuildConfig
import com.bogtech.network.account.AccountsDao
import com.bogtech.network.feed.FeedDao
import com.bogtech.network.savings.SavingsDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main access point for remote or local instances of API services.
 */
class ApiManager {

    // Get retrofit with converterFactory and OkHttpClient.
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    /**
     * Returns Accounts Data Access Object with default observeOn UI Thread and subscribeOn IO Thread
     * to allow host app to update UI directly without getting exception that a background thread is trying to update the UI.
     */
    fun getAccountsDao(): AccountsDao {
        return AccountsDao(getRetrofit())
    }

    fun getFeedDao(): FeedDao {
        return FeedDao(getRetrofit())
    }

    fun getSavingsDao(): SavingsDao {
        return SavingsDao(getRetrofit())
    }

    private var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(setupInterceptor())
        .build()

    private fun setupInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    companion object {
        private var BASE_URL = BuildConfig.BASE_URL
        const val token = BuildConfig.CLIENT_SECRET
    }
}