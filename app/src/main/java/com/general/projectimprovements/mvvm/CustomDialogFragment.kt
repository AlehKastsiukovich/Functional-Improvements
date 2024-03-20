package com.general.projectimprovements.mvvm

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.intellimec.oneapp.common.mvvm.BaseViewModel
import com.general.projectimprovements.view.WindowController

abstract class CustomDialogFragment<VM : BaseViewModel, VB : ViewBinding>(
    @LayoutRes
    val layoutResId: Int,
    val windowController: WindowController,
    viewModelProvider: (fragment: Fragment) -> VM
) : DialogFragment() {

    @VisibleForTesting
    abstract val fragmentViewBinding: FragmentViewBinding<VB>

    @get:StyleRes
    abstract val styleId: Int

    protected val fragmentBinding: VB?
        get() = fragmentViewBinding.get()

    protected val viewModel by lazy { viewModelProvider(this) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), styleId)
        dialog.window?.also { it.requestFeature(Window.FEATURE_NO_TITLE) }
        val view = fragmentBinding?.root ?: getContentView()
        fragmentViewBinding.onViewCreated(view)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        if (fragmentBinding?.root == null) {
            val view = getContentView()
            Pair(view, viewLifecycleOwner.lifecycle)
        } else {
            val view = super.onCreateView(inflater, container, savedInstanceState)
            Pair(view, lifecycle)
        }.let { pair ->
            pair.second.addObserver(object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    setupWindow(windowController)
                }
                override fun onDestroy(owner: LifecycleOwner) {
                    owner.lifecycle.removeObserver(this)
                }
            })
            pair.first
        }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewBinding.onDestroyView()
    }

    private fun getContentView(): View = layoutInflater.inflate(layoutResId, null, false).also { view ->
        fragmentViewBinding.onViewCreated(view)
    }

    private fun setupWindow(windowController: WindowController) {
        windowController.setupNoAppBars { window ->
            setupWindow(window)
        }
    }

    abstract fun setupWindow(window: Window)
}
