package com.bogtech.network

import com.bogtech.network.account.model.Account

interface Sdk {

    fun getAccounts(): List<Account>

//    fun getTransactionFeed(): List<Something>

    fun getSavingsGoals()

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