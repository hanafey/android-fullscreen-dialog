package com.hanafey.example.theming.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.hanafey.android.DialogFragmentListener
import com.hanafey.android.dlog
import com.hanafey.example.theming.App
import com.hanafey.example.theming.R
import com.hanafey.example.theming.data.PreferenceRepository
import com.hanafey.example.theming.ui.fullscreendialog.ExampleDialog

class MainActivity : AppCompatActivity(), DialogFragmentListener {
    private val LTAG = this::class.java.simpleName
    private var dialog = 1

    private val svm: OonSharedViewModel by viewModels()
    private lateinit var toolbar: Toolbar
    private lateinit var dialogResult: TextView
    private lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        dialogResult = findViewById(R.id.dialog_result)
        dialogResult.text = svm.scoreAsString()
        dialog = 1
        val button = findViewById<MaterialButton>(R.id.button)
        button.setOnClickListener { openDialog() }

        preferenceRepository = (application as App).preferenceRepository
        preferenceRepository.nightModeLive
            .observe(this, Observer { mode ->
                delegate.localNightMode = mode
            })
        dlog(LTAG) { "onCreate: score=${svm.scoreAsString()} dialogResult=${dialogResult.hashCode()}" }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        menu.findItem(R.id.MA_menu_dark)?.let {
            it.isChecked = preferenceRepository.isDarkTheme
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.MA_menu_dark -> {
                item.isChecked = !item.isChecked
                preferenceRepository.isDarkTheme = item.isChecked
                false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        dlog(LTAG) { "onStart: score=${svm.scoreAsString()} dialogResult=${dialogResult.hashCode()}" }
    }

    override fun onResume() {
        super.onResume()
        dlog(LTAG) { "onResume: score=${svm.scoreAsString()} dialogResult=${dialogResult.hashCode()}" }
    }

    private fun openDialog() {
        when (dialog) {
            0 -> ExampleDialog.display(supportFragmentManager)
            1 -> OneDialogFragment.instance("junk", svm::class.java).show(supportFragmentManager, "ONE")
            else -> throw IllegalStateException("Unexpected value: $dialog")
        }
    }

    override fun message(tag: String, intent: Intent) {
        when (tag) {
            "ONE" -> {
                dialogResult.text = when (intent) {
                    DialogFragmentListener.RESULT_OK -> svm.scoreAsString()
                    DialogFragmentListener.RESULT_CANCEL -> "..."
                    else -> "?"
                }
            }
        }
    }
}
