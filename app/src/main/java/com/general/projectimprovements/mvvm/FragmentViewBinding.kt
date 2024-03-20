package com.general.projectimprovements.mvvm

import android.view.View
import androidx.viewbinding.ViewBinding

class FragmentViewBinding<T : ViewBinding>(
    private val bind: (View) -> T,
    private val initViews: (T) -> Unit = {},
    private val clearViews: (T) -> Unit = {}
) {

    private var viewBinding: T? = null

    fun get(): T? = viewBinding

    fun onViewCreated(view: View) {
        viewBinding = bind(view)
        viewBinding?.let { initViews(it) }
    }

    fun onDestroyView() {
        viewBinding?.let { clearViews(it) }
        viewBinding = null
    }
}
