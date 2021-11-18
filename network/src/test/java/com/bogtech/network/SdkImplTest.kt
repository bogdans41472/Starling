package com.bogtech.network

import com.bogtech.network.account.AccountsDao
import com.bogtech.network.account.model.Account
import com.bogtech.network.account.model.AccountsList
import com.bogtech.network.network.ApiManager
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Scheduler
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.Retrofit

class SdkImplTest {

    val mockedHttpClient = Mockito.mock(OkHttpClient::class.java)
    val apiManager = ApiManager(mockedHttpClient)
    val accountsDao = Mockito.mock(AccountsDao::class.java)
    val accountsList = Mockito.mock(AccountsList::class.java)
    val mockRetrofit = Mockito.mock(Retrofit::class.java)
    val mockScheduler = Mockito.mock(DefaultRxSchedulers::class.java)
    val mockUiScheduler = Mockito.mock(Scheduler::class.java)

    lateinit var sdk: SdkImpl

    val mockAccountsList = Mockito.mock(AccountsList::class.java)

    val account = Account("accountUid",
        "type",
        "category",
        "GBP",
        "1999",
        "name")
    val account1 = Account("differentAccountUid",
        "type",
        "category",
        "GBP",
        "1999",
        "name")
    val accountList = AccountsList(listOf(account, account1, account1))

    @Before
    fun setUp() {
        sdk = SdkImpl.getInstance()
        sdk.apiManager = apiManager
    }

    @Ignore("TicketNumber - schedulers are not applying correctly ")
    @Test
    fun `getAccountList success`() {
        whenever(accountsDao.getAccountsList()).thenReturn(Single.just(accountList))
        whenever(accountsDao.rxSchedulers).thenReturn(mockScheduler)

        whenever(mockScheduler.ui()).thenReturn(mockUiScheduler)
        whenever(mockScheduler.io()).thenReturn(mockUiScheduler)

        sdk.getAccountsList().test()
            .assertValue(accountList)
    }

    @Test
    fun `getMainAccount success`() {
        assertEquals(account, sdk.getMainAccount(accountList))
    }
}