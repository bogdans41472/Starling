package com.bogtech.network.network

import androidx.annotation.VisibleForTesting
import com.bogtech.network.exception.InternalError
import com.bogtech.network.exception.InternalException
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Single
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

/**
 * BaseDao is responsible for providing base utilities to all DAO objects.
 * This ensures that all errors associated to DAOs come through the same place.
 */
open class BaseDao {

    @VisibleForTesting
    var rxSchedulers = DefaultRxSchedulers()

    fun <T> getDefaultSingle(single: Single<T>): Single<T> {
        return single
            .observeOn(rxSchedulers.ui())
            .subscribeOn(rxSchedulers.io())
            .onErrorResumeNext(this::handleError)
    }

    private fun <T> handleError(throwable: Throwable): Single<T> {
        if (isGeneralConnectivityError(throwable)) {
            return Single.error(
                InternalException(
                    InternalError.NETWORK_ERROR,
                    "There was a problem with your connection, please check your connection",
                    throwable
                )
            )
        }
        if (isHttpError(throwable)) {
            return handleStarlingSpecificError(throwable as HttpException)
        }

        return Single.error(
            InternalException(
                InternalError.UNKNOWN_ERROR,
                "An unknown error has occurred",
                throwable
            )
        )
    }

    private fun <T> handleStarlingSpecificError(error: HttpException): Single<T> {
        return when (error.code()) {
            403 -> Single.error(
                InternalException(
                    InternalError.FORBIDDEN,
                    "Token has expired, please update your token in network/build.gradle"
                )
            )
            400 -> Single.error(
                InternalException(
                    InternalError.BAD_REQUEST,
                    "Bad request: ${error.message}"
                )
            )
            else -> Single.error(
                InternalException(
                    InternalError.UNKNOWN_ERROR,
                    "ErrorCode: ${error.code()} " +
                            "Message: ${error.message()}"
                )
            )
        }
    }

    private fun isHttpError(throwable: Throwable) =
        throwable is HttpException

    private fun isGeneralConnectivityError(throwable: Throwable) =
        throwable is IOException || throwable is UnknownHostException
}