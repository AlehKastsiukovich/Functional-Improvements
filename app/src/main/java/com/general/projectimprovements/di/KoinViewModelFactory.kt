package com.general.projectimprovements.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class KoinViewModelFactory(
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val scope: Scope
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = create(
        modelClass = modelClass,
        extras = CreationExtras.Empty
    )

    @Suppress("UNCHECKED_CAST", "TooGenericExceptionCaught")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = try {
        scope.get(
            clazz = modelClass.kotlin,
            qualifier = null,
            parameters = { parametersOf(activity, fragment) }
        )
    } catch (koinThrowable: Throwable) {
        try {
            modelClass.newInstance()
        } catch (_: Throwable) {
            throw koinThrowable
        }
    }
}

inline fun <reified T : ViewModel> Module.viewModelFactory(noinline definition: ViewModelDefinition<T>): KoinDefinition<T> =
    factory { (activity: FragmentActivity, fragment: Fragment) ->
        definition(activity, fragment)
    }

typealias ViewModelDefinition<T> = Scope.(FragmentActivity, Fragment) -> T
