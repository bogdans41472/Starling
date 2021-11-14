package com.bogtech.network.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Schedulers used for observing and subscribing on.
 */
class DefaultRxSchedulers : RxSchedulers {
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }
}

private interface RxSchedulers {
    fun ui(): Scheduler
    fun io(): Scheduler
}