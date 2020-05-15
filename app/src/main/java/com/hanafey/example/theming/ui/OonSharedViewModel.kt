package com.hanafey.example.theming.ui

import androidx.lifecycle.ViewModel
import com.hanafey.android.dlog

class OonSharedViewModel : ViewModel() {
    private val LTAG = this::class.java.simpleName
    val toolbarTitle = "1 of 9 Score"
    val variableTitle = "Zombie Level (Z score)"
    var score = 0.0

    fun scoreAsString(): String {
        var x = "%1.1f".format(score)
        return x
    }

    override fun onCleared() {
        dlog(LTAG) { "onCleared()" }
        super.onCleared()
    }
}
