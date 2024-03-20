package com.intellimec.oneapp.common.mvvm

import kotlinx.coroutines.flow.Flow

interface BaseViewModel {
    val loading: Flow<Boolean>
    fun onResume()
}
