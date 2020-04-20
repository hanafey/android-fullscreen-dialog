package io.alexanderschaefer.fullscreendialog

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.hanafey.example.OneDialogFragment

class MainActivity : AppCompatActivity() {
    private var dialog = 0
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        dialog = 1
        val button = findViewById<MaterialButton>(R.id.button)
        button.setOnClickListener { v: View? -> openDialog() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    private fun openDialog() {
        when (dialog) {
            0 -> ExampleDialog.display(supportFragmentManager)
            1 -> OneDialogFragment().show(supportFragmentManager, "ONE")
            else -> throw IllegalStateException("Unexpected value: $dialog")
        }
    }
}
