package com.bogtech.network

import android.content.Context
import com.bogtech.network.account.model.Account

class SdkImpl: Sdk {

    fun initializeWithContext(context: Context) {
        // initialize ApiManager
        // Setu prequisites
    }

    override fun getAccounts(): List<Account> {
        TODO("Not yet implemented")
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