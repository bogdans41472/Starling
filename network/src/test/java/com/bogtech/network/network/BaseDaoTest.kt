package com.bogtech.network.network

import com.bogtech.network.exception.InternalError
import com.bogtech.network.exception.InternalException
import com.bogtech.network.util.DefaultRxSchedulers
import io.reactivex.Scheduler
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import okhttp3.MediaType
import okhttp3.ResponseBody

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class BaseDaoTest {

    val baseDao = BaseDao()
    val schedulers = Mockito.mock(DefaultRxSchedulers::class.java)
    val mockScheduler = Mockito.mock(Scheduler::class.java)

    @Before
    fun setUp() {
        whenever(schedulers.ui()).thenReturn(mockScheduler)
        whenever(schedulers.io()).thenReturn(mockScheduler)
        baseDao.rxSchedulers = schedulers
    }

    @Test
    fun `getDefaultSingle handles IOException`() {
        val singleForTest: Single<String> = Single.just("Foo")

        checkErrorIsReturned(singleForTest, IOException(), InternalError.NETWORK_ERROR)
    }

    @Test
    fun `getDefaultSingle handles UnknownHostException`() {
        val singleForTest: Single<String> = Single.just("Foo")

        checkErrorIsReturned(singleForTest, UnknownHostException(), InternalError.NETWORK_ERROR)
    }

    @Test
    fun `getDefaultSingle handles Http403 error`() {
        val singleForTest: Single<String> = Single.just("Foo")
        val response: Response<String> = Response.error(
            403, ResponseBody.create(
                MediaType.get("string/url"), "sd"
            )
        )

        checkErrorIsReturned(singleForTest, HttpException(response), InternalError.FORBIDDEN)
    }

    @Test
    fun `getDefaultSingle handles Http400 error`() {
        val singleForTest: Single<String> = Single.just("Foo")
        val response: Response<String> = Response.error(
            400, ResponseBody.create(
                MediaType.get("string/url"), "sd"
            )
        )

        checkErrorIsReturned(singleForTest, HttpException(response), InternalError.BAD_REQUEST)
    }

    @Test
    fun `getDefaultSingle handles unknown HttpCode error`() {
        val singleForTest: Single<String> = Single.just("Foo")
        val response: Response<String> = Response.error(
            401, ResponseBody.create(
                MediaType.get("string/url"), "sd"
            )
        )

        checkErrorIsReturned(singleForTest, HttpException(response), InternalError.UNKNOWN_ERROR)
    }

    private fun checkErrorIsReturned(
        singleForTest: Single<String>,
        exception: Exception,
        expectedEnum: InternalError
    ) {
        baseDao.getDefaultSingle(singleForTest)
            .doOnSubscribe { throw exception }
            .subscribe({

            }, {
                assertTrue(it is InternalException)
                assertTrue((it is InternalException) && it.errorEnum == expectedEnum)
            })
    }
}