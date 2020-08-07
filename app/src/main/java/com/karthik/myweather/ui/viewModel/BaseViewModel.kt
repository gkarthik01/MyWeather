package com.karthik.myweather.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    @JvmField
    val isLoading = MutableLiveData<Boolean>()

    @JvmField
    val error = MutableLiveData<String>()

    companion object {
        const val Error = "Error"
    }
}