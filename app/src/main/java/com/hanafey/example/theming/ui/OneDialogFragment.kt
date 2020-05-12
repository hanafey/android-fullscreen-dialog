package com.hanafey.example.theming.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.hanafey.example.theming.R
import kotlin.math.roundToInt

class OneDialogFragment : DialogFragment(), View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener {
    private lateinit var toolbar: Toolbar
    private lateinit var numberButtons: List<MaterialButton>
    private lateinit var valueText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Confirm - this is just to block standard style being applied (which happens
        //       if zero is passed for the style resource.
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.a_dialog, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.dialog_oon_title)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }

        numberButtons = listOf(
            R.id.OON_0, R.id.OON_1, R.id.OON_2, R.id.OON_3, R.id.OON_4, R.id.OON_5,
            R.id.OON_6, R.id.OON_7, R.id.OON_8, R.id.OON_9
        ).map { id ->
            val bv = view.findViewById<MaterialButton>(id)
            bv.setOnClickListener(this)
            bv
        }

        view.findViewById<MaterialButtonToggleGroup>(R.id.OON_fraction_toggle).addOnButtonCheckedListener(this)
        valueText = view.findViewById(R.id.OON_value)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            // TODO: Verify not needed.
            // dialog.window!!.setLayout(width, height)
            dialog.window!!.setWindowAnimations(R.style.AppTheme_SlideInOut)
        }
    }

    companion object {
        private const val LTAG = "OneDialogFragment"
    }

    override fun onClick(v: View?) {
        val ix = numberButtons.indexOf(v)
        if (ix >= 0) {
            val currentValue = valueText.text.toString().toDouble()
            var intPart = currentValue.toInt()
            var fracPart = ((currentValue - intPart) * 10).roundToInt()
            if (!numberButtons[0].isEnabled) {
                // Whole number part being set
                intPart = ix
            } else {
                fracPart = ix
            }
            valueText.text = if (fracPart != 0) "$intPart.$fracPart" else "$intPart"
        }
    }

    override fun onButtonChecked(group: MaterialButtonToggleGroup?, checkedId: Int, isChecked: Boolean) {
        if (isChecked) {
            val wholeNumbers = checkedId == R.id.OON_whole
            numberButtons.forEachIndexed() { ix, bt ->
                val label = if (wholeNumbers) "$ix" else "0.$ix"
                if (bt.text != label) bt.text = label
            }

            numberButtons[0].isEnabled = !wholeNumbers
        }
    }
}
