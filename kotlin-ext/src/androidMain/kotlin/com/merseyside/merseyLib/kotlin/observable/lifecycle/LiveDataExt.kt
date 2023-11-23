package com.merseyside.merseyLib.kotlin.observable.lifecycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.merseyside.merseyLib.kotlin.observable.ObservableField

fun <T> ObservableField<T>.asLiveData(ignoreCurrent: Boolean = false): LiveData<T> {
    val liveData = MutableLiveData<T>()

    observe(ignoreCurrent) { value ->
        liveData.value = value
    }

    return liveData
}