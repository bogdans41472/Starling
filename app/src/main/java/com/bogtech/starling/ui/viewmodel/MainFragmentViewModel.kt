package com.bogtech.starling.ui.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogtech.network.Sdk
import com.bogtech.network.exception.InternalException
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.savings.model.SavingsGoals
import com.bogtech.network.savings.model.SavingsGoalsAmount
import com.bogtech.network.savings.model.SavingsTarget
import com.bogtech.network.savings.model.TransferResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainFragmentViewModel : ViewModel(), DefaultLifecycleObserver {

    val totalRoundUpAmountLiveData: MutableLiveData<Amount> = MutableLiveData()
    val transferState: MutableLiveData<TransferResponse> = MutableLiveData()
    val loadingProcess: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveData: MutableLiveData<InternalException> = MutableLiveData()

    private lateinit var sdk: Sdk
    private lateinit var compositeDisposable: CompositeDisposable

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

    /**
     * Calls into SDK to retrieve the roundUp total for last week
     */
    private fun refreshRoundUpTotal() {
        compositeDisposable.add(
            sdk.getRoundUpForLastWeek()
                .doOnSubscribe{ loadingProcess.postValue(true) }
                .subscribe({
                    loadingProcess.postValue(false)
                    totalRoundUpAmountLiveData.postValue(it)
                }, {
                    loadingProcess.postValue(false)
                    errorLiveData.postValue(it as InternalException)
                })
        )
    }

    fun transferRoundUpAmount() {
        compositeDisposable.add(
            sdk.createSavingsGoal(
                createNewSavingsGoals())
                .doOnSubscribe { loadingProcess.postValue(true) }
                .flatMap { savingsGoalsResponse ->
                    sdk.addMoneyToSavings(
                        savingsGoalsResponse.savingsGoalUid,
                        UUID.randomUUID().toString(),
                        // Ideally this amount would be retrieved from somewhere
                        // instead of being hardcoded
                        SavingsGoalsAmount(Amount("GBP", 1033L))
                    )}
                .subscribe({ response ->
                    transferState.postValue(response)
                    loadingProcess.postValue(false)
                }, {
                    errorLiveData.postValue(it as InternalException)
                    loadingProcess.postValue(false)
                })
        )
    }

    private fun createNewSavingsGoals() = SavingsGoals(
        "Secret Lab",
        "GBP",
        SavingsTarget("GBP", 10000),
        "asda"
    )
}