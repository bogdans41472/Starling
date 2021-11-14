package com.bogtech.network

import android.content.Context
import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.account.service.RemoteAccountApi
import com.bogtech.network.feed.model.FeedItemList
import com.bogtech.network.network.ApiManager
import io.reactivex.Single

class SdkImpl: Sdk {

    val apiManager: ApiManager = ApiManager()

    fun initializeWithContext(context: Context) {
        // Setup prequisites
    }

    override fun getAccountsList(): Single<AccountsList> {
        return apiManager.getAccountsDao()
    }

    override fun getTransactionFeed(accountUid: String, category: String): Single<FeedItemList> {
        return apiManager.getFeedDao(accountUid, category, null)
    }

    override fun getSavingsGoals() {
        TODO("Not yet implemented")
    }

    companion object {
        fun getInstance() : SdkImpl {
            return Holder.INSTANCE
        }

        private object Holder {
            val INSTANCE by lazy { SdkImpl() }
        }
    }
}