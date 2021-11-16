package com.bogtech.network.account

import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.account.service.RemoteAccountApi
import com.bogtech.network.exception.InternalError
import com.bogtech.network.exception.InternalException
import io.reactivex.Single
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException

class AccountsDao(
    private val retrofit: Retrofit
) {

    fun getAccountsList(): Single<AccountsList> {
        return retrofit.create(RemoteAccountApi::class.java).getAccountLists()
            .onErrorResumeNext(this::handleError)
    }

    private fun handleError(throwable: Throwable): Single<AccountsList> {
        if (throwable is IOException || throwable is UnknownHostException) {
            return Single.error(InternalException(InternalError.NETWORK_ERROR,
                "There was a problem with your connection, please check your connection")
            )
        }

        return Single.error(throwable)
    }
}