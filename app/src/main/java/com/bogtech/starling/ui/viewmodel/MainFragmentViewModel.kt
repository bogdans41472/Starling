package com.bogtech.starling.ui.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogtech.network.Sdk
import com.bogtech.network.feed.model.subtypes.Amount
import io.reactivex.disposables.CompositeDisposable

class MainFragmentViewModel : ViewModel(), DefaultLifecycleObserver {

    val totalRoundUpAmountLiveData: MutableLiveData<Amount> = MutableLiveData()
    val errorLiveData: MutableLiveData<Throwable> = MutableLiveData()

    private lateinit var sdk: Sdk
    private lateinit var compositeDisposable: CompositeDisposable

    fun refreshRoundUpTotal(
        minTimestamp: String = "2020-06-01T12:34:56.000Z",
        maxTimestamp: String = "2020-07-01T12:34:56.000Z"
    ) {
        compositeDisposable.add(
            sdk.getRoundUpBetweenTimestamps(
                minTimestamp,
                maxTimestamp
            )
                .subscribe({
                    totalRoundUpAmountLiveData.postValue(it)
                }, {
                    errorLiveData.postValue(it)
                })
        )
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        sdk = Sdk.getInstance()
        compositeDisposable = CompositeDisposable()
        refreshRoundUpTotal()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        compositeDisposable.dispose()
    }
}