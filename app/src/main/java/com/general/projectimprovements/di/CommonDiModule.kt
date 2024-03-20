package com.general.projectimprovements.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.viewmodel.CreationExtras
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

val commonUiModule = module {

    viewModelFactory { activity: FragmentActivity, fragment: Fragment, appAnalytics: AppAnalytics ->
        val args = GenericDialogFragmentArgs.fromBundle(fragment.requireArguments())
        GenericDialogViewModelImpl(
            appAnalytics = appAnalytics,
            params = args.dialogParams,
            confirmSharedViewModel = ViewModelProvider(activity).get<ConfirmSharedViewModelImpl>()
        )
    }

    fragmentFactory { activity: FragmentActivity, _: WindowController, appAnalytics: AppAnalytics ->
        GenericDialogFragment(androidService = get()) { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(activity, fragment, appAnalytics) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get<GenericDialogViewModelImpl>()
        }
    }

    viewModelFactory { activity: FragmentActivity, fragment: Fragment
        val args = ConfirmDialogFragmentArgs.fromBundle(fragment.requireArguments())
        ConfirmDialogViewModelImpl(
            appAnalytics = appAnalytics,
            confirmSharedViewModel = ViewModelProvider(activity).get<ConfirmSharedViewModelImpl>(),
            viewState = args.dialogParams,
            dialogType = ConfirmDialogType.valueOf(args.dialogType)
        )
    }

    fragmentFactory { activity: FragmentActivity, _: WindowController, appAnalytics: AppAnalytics ->
        ConfirmDialogFragment(
            androidService = get()
        ) { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(activity, fragment, appAnalytics) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get<ConfirmDialogViewModelImpl>()
        }
    }
}

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
