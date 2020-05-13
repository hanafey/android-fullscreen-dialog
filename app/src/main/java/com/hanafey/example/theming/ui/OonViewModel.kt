package com.hanafey.example.theming.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OonViewModel : ViewModel() {
    private val wholeNumberModeMLD = MutableLiveData(true)
    private val oonValueMLD = MutableLiveData("0")

    val wholeNumberModeLiveData: LiveData<Boolean>
        get() = wholeNumberModeMLD

    var isWholeNumberMode
        get() = wholeNumberModeMLD.value!!
        set(value) {
            wholeNumberModeMLD.value = value
        }

    var oonValue
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
}

