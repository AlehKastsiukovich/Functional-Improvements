package com.general.projectimprovements.mvvm

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.intellimec.oneapp.common.mvvm.BaseViewModel

abstract class BaseDialogFragment<VM : BaseViewModel>(
    viewModelProvider: (Fragment) -> VM
) : DialogFragment() {

    protected val viewModel by lazy { viewModelProvider(this) }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }
}
