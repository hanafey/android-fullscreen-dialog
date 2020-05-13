package com.hanafey.example.theming.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.hanafey.example.theming.R
import kotlin.math.roundToInt

class OneDialogFragment :
    DialogFragment(),
    View.OnClickListener,
    View.OnLongClickListener,
    MaterialButtonToggleGroup.OnButtonCheckedListener {

    companion object {
        private const val ARG_oon_title = "arg_oon_title"
        private const val ARG_oon_shared_view_model = "arg_shared_view_model"

        private const val LTAG = "OneDialogFragment"
        fun instance(oonTitle: String, sharedViewModelClass: Class<out OonSharedViewModel>): OneDialogFragment {
            val obj = OneDialogFragment()
            obj.arguments = Bundle().apply {
                putString(ARG_oon_title, oonTitle)
                putSerializable(ARG_oon_shared_view_model, sharedViewModelClass)
            }
            return obj
        }
    }

    private lateinit var svm: OonSharedViewModel
    private lateinit var toolbar: Toolbar
    private lateinit var numberButtons: List<MaterialButton>
    private val vm: OonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Confirm - this is just to block standard style being applied (which happens
        //       if zero is passed for the style resource.
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
        arguments.let { args ->

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.a_dialog, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = svm.toolbarTitle
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
        toolbar.setOnMenuItemClickListener {
            dismiss()
            true
        }
        view.findViewById<TextView>(R.id.OON_title).text = svm.variableTitle

        numberButtons = listOf(
            R.id.OON_0, R.id.OON_1, R.id.OON_2, R.id.OON_3, R.id.OON_4, R.id.OON_5,
            R.id.OON_6, R.id.OON_7, R.id.OON_8, R.id.OON_9
        ).map { id ->
            val bv = view.findViewById<MaterialButton>(id)
            bv.setOnClickListener(this)
            bv.setOnLongClickListener(this)
            bv
        }

        view.findViewById<MaterialButtonToggleGroup>(R.id.OON_fraction_toggle).addOnButtonCheckedListener(this)
        val valueText: TextView = view.findViewById(R.id.OON_value)

        vm.oonValueLive.observe(this, Observer<String>() { value ->
            valueText.text = value
        })

        vm.wholeNumberModeLiveData.observe(this, Observer<Boolean> { isWhole ->
            numberButtons.forEachIndexed() { ix, bt ->
                val label = if (isWhole) "$ix" else "0.$ix"
                if (bt.text != label) bt.text = label
            }
            // numberButtons[0].isEnabled = !isWhole
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val svmc =
            (arguments?.getSerializable(ARG_oon_shared_view_model)
                ?: throw IllegalStateException("You must create dialog with static instance call.")) as Class<OonSharedViewModel>
        svm = ViewModelProvider(requireActivity()).get(svmc)
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

    override fun onClick(v: View?) {
        onClickWithModeOverride(v, false)
    }

    override fun onLongClick(v: View?): Boolean {
        onClickWithModeOverride(v, true)
        return true
    }

    fun onClickWithModeOverride(v: View?, override: Boolean) {
        val ix = numberButtons.indexOf(v)
        if (ix >= 0) {
            val currentValue = vm.oonValueAsDouble
            var intPart = currentValue.toInt()
            var fracPart = ((currentValue - intPart) * 10).roundToInt()
            if (vm.isWholeNumberMode xor override) {
                // Whole number part being set
                intPart = ix
            } else {
                fracPart = ix
            }
            vm.oonValue = if (fracPart != 0) "$intPart.$fracPart" else "$intPart"
        }
    }

    override fun onButtonChecked(group: MaterialButtonToggleGroup?, checkedId: Int, isChecked: Boolean) {
        if (isChecked) {
            vm.isWholeNumberMode = checkedId == R.id.OON_whole
        }
    }

}
