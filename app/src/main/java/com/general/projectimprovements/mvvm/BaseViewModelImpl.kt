package com.general.projectimprovements.mvvm

import androidx.lifecycle.ViewModel
import com.intellimec.oneapp.common.mvvm.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

open class BaseViewModelImpl(
    protected val appAnalytics: AppAnalytics,
    private val navigationEvent: NavigationEvent
) : ViewModel(), BaseViewModel {

    override val loading = MutableSharedFlow<Boolean>()

    override fun onResume() {
        appAnalytics.logCurrentScreen(navigationEvent)
    }
}
