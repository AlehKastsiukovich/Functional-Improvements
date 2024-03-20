package com.general.projectimprovements.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.intellimec.oneapp.common.mvvm.BaseViewModel
import com.general.projectimprovements.view.WindowController
import com.general.projectimprovements.view.setDefault
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>(
    @LayoutRes layoutId: Int,
    private val windowController: WindowController,
    viewModelProvider: (Fragment) -> VM
) : Fragment(layoutId) {

    protected val viewModel by lazy { viewModelProvider(this) }

    open val toolbarTitle: String? = ""

    @VisibleForTesting
    abstract val fragmentViewBinding: FragmentViewBinding<VB>

    protected val fragmentBinding: VB?
        get() = fragmentViewBinding.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                setupWindow(windowController)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
            }
        })

        fragmentViewBinding.onViewCreated(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentViewBinding.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()

        toolbarTitle?.takeIf { it.isNotEmpty() }
            ?.let { windowController.setToolbarTitle(it) }
    }

    protected open fun setupWindow(windowController: WindowController) {
        windowController.setup(showToolbar = true) { window ->
            window.setDefault()
        }
    }

    protected fun <T> Flow<T>.collectWithViewLifecycle(minActiveState: Lifecycle.State = Lifecycle.State.STARTED, block: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            flowWithLifecycle(viewLifecycleOwner.lifecycle, minActiveState)
                .collect { block(it) }
        }
    }
}
