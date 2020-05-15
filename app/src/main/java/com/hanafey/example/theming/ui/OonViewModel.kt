package com.hanafey.example.theming.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hanafey.android.dlog

class OonViewModel : ViewModel() {
    private val LTAG = this::class.java.simpleName
    private val wholeNumberModeMLD = MutableLiveData(true)
    private val oonValueMLD = MutableLiveData("0")

    val wholeNumberModeLiveData: LiveData<Boolean>
        get() = wholeNumberModeMLD

    var isWholeNumberMode: Boolean
        get() = wholeNumberModeMLD.value!!
        set(value) {
            wholeNumberModeMLD.value = value
        }

    var oonValue: String
        get() = oonValueMLD.value!!
        set(value) {
            oonValueMLD.value = value
        }

    val oonValueLive: LiveData<String>
        get() = oonValueMLD

    val oonValueAsDouble: Double
        get() {
            return if (oonValue.isBlank()) {
                0.0
            } else {
                try {
                    oonValue.toDouble()
                } catch (ex: Exception) {
                    0.0
                }
            }

        }

    override fun onCleared() {
        dlog(LTAG) { "onCleared()" }
        super.onCleared()
    }
}

