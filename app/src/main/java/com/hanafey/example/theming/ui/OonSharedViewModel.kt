package com.hanafey.example.theming.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class OonSharedViewModel(app: Application) : AndroidViewModel(app) {
    val toolbarTitle = "1 of 9 Score"
    val variableTitle = "Zombie Level (Z score)"
    var score = 0.0

    fun scoreAsString(): String {
        var x = "%1.1f".format(score)
        return x
    }
}
