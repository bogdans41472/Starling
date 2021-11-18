package com.bogtech.network.account

import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.account.service.RemoteAccountApi
import com.bogtech.network.network.BaseDao
import io.reactivex.Single
import retrofit2.Retrofit

/**
 * AccountsDao is responsible for making /account calls via RemoteAccountApi retrofit.
 */
open class AccountsDao(
    private val retrofit: Retrofit
) : BaseDao() {

    fun getAccountsList(): Single<AccountsList> {
        return getDefaultSingle(
            retrofit.create(RemoteAccountApi::class.java)
                .getAccountLists()
        )
    }
}