package com.merseyside.merseyLib.kotlin.observable.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.merseyLib.kotlin.observable.ObservableField

fun <T> ObservableField<T>.asLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()

    observe(ignoreCurrent = false) { value ->
        liveData.value = value
    }

    return liveData
}