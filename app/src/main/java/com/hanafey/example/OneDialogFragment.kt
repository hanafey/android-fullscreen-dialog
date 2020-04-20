package com.hanafey.example

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import io.alexanderschaefer.fullscreendialog.R

class OneDialogFragment : DialogFragment() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        if (v != null) Log.e(LTAG, "Unexpected non-null view!")
        return inflater.inflate(R.layout.a_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.dialog_oon_title)
        toolbar.setNavigationOnClickListener { v: View? -> dismiss() }
        toolbar.inflateMenu(R.menu.oon_dialog_menu)
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_SlideInOut)
        }
    }

    companion object {
        private const val LTAG = "OneDialogFragment"
    }
}
