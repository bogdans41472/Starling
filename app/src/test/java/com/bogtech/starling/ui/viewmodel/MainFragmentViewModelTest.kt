package com.bogtech.starling.ui.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bogtech.network.Sdk
import com.bogtech.network.exception.InternalError
import com.bogtech.network.exception.InternalException
import com.bogtech.network.feed.model.subtypes.Amount
import com.bogtech.network.savings.model.TransferResponse
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class MainFragmentViewModelTest {
    // necessary for LiveData observing
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()


    val sdk: Sdk = Mockito.mock(Sdk::class.java)
    val viewModel = MainFragmentViewModel(sdk)

    val lifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
    @Mock lateinit var totalRoundUpObserver : Observer<Amount>
    @Mock lateinit var transferStateObserver : Observer<TransferResponse>
    @Mock lateinit var progressDialogObserver : Observer<Boolean>

    @Mock lateinit var exceptionObserver : Observer<InternalException>

    @Test
    fun `onStart set up disposable`() {
        viewModel.onStart(lifecycleOwner)

        assertTrue(viewModel.compositeDisposable != null)
        assertFalse(viewModel.compositeDisposable.isDisposed)
    }

    @Test
    fun `onStop disposes of disposable`() {
        viewModel.onStart(lifecycleOwner)

        viewModel.onStop(lifecycleOwner)

        assertTrue(viewModel.compositeDisposable.isDisposed)
    }

    @Test
    fun `refreshRoundTotal throws Error THEN sends error via LiveData`() {
        setupObservers()
        val expectedException = InternalException(InternalError.NETWORK_ERROR, "")
        whenever(sdk.getRoundUpForLastWeek())
            .thenReturn(Single.error(expectedException))
        viewModel.onStart(lifecycleOwner)

        viewModel.refreshRoundUpTotal()

        val exceptionCaptor = ArgumentCaptor.forClass(InternalException::class.java)
        exceptionCaptor.run {
            verify(exceptionObserver, times(1)).onChanged(capture())
            assertEquals(expectedException, this.value)
        }

        val loadingIndicatorCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        loadingIndicatorCaptor.run {
            verify(progressDialogObserver, times(2)).onChanged(capture())
        }
    }

    @Test
    fun `refreshRoundTotal is successful THEN`() {
        setupObservers()
        val expectedAmount = Amount("GBP", 1312)
        whenever(sdk.getRoundUpForLastWeek())
            .thenReturn(Single.just(expectedAmount))
        viewModel.onStart(lifecycleOwner)

        viewModel.refreshRoundUpTotal()

        val captor = ArgumentCaptor.forClass(Amount::class.java)
        captor.run {
            verify(totalRoundUpObserver, times(1)).onChanged(capture())
            assertEquals(expectedAmount, this.value)
        }

        val loadingIndicatorCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        loadingIndicatorCaptor.run {
            verify(progressDialogObserver, times(2)).onChanged(capture())
        }
    }

    private fun setupObservers() {
        viewModel.totalRoundUpAmountLiveData.observeForever(totalRoundUpObserver)
        viewModel.transferState.observeForever(transferStateObserver)
        viewModel.loadingProcess.observeForever(progressDialogObserver)
        viewModel.errorLiveData.observeForever(exceptionObserver)
    }
}