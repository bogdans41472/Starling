package com.bogtech.starling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bogtech.network.Sdk

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAccountsList()
    }

    private val sdk = Sdk.getInstance()

    private fun getAccountsList() {
        sdk.getAccountsList()
            .doAfterSuccess { listOfAccounts ->
                val accounts = listOfAccounts.accounts
                if (accounts.isNotEmpty()) {
                    val mainAccount = accounts[0]
                    sdk.getTransactionFeed(mainAccount.accountUid, mainAccount.defaultCategory)
                        .subscribe({
                            Log.i("Bogdan", "itemList: ${it.feedItems[0].amount}")
                        }, {

                        })
                }
            }
            .subscribe()
    }
}