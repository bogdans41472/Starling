package com.bogtech.starling.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") // the cast is checked via `isAssignableFrom()` method
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            MainFragmentViewModel() as T
        } else {
            throw IllegalArgumentException("$modelClass is not supported by this Factory")
        }
    }
}