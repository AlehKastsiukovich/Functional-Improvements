package com.general.projectimprovements.view

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun Fragment.resetFocus() = activity?.currentFocus?.resetFocus()

fun Fragment.addOnBackPressedCallback(onBackPressed: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    )
}

fun launchPermissionsResult(
    permissionResultLauncher: ActivityResultLauncher<ContinuationPermissionsInput>
): suspend (Collection<String>) -> Boolean? = { manifestPermission ->
    suspendCoroutine { continuation ->
        permissionResultLauncher.launch(
            ContinuationPermissionsInput(
                manifestPermissions = manifestPermission,
                continuation = continuation
            )
        )
    }
}

fun launchActivityResult(activityResultLauncher: ActivityResultLauncher<ContinuationStartActivityInput>): suspend (Intent) -> ActivityResult =
    { intent ->
        suspendCoroutine { continuation ->
            activityResultLauncher.launch(
                ContinuationStartActivityInput(
                    intent = intent,
                    continuation = continuation
                )
            )
        }
    }

fun Fragment.registerForMultiplePermissionsActivityResult(): ActivityResultLauncher<ContinuationPermissionsInput> = this.registerForActivityResult(
    RequestMultiplePermissionsDontAskAgain()
) { result ->
    result.continuation?.resume(result.result)
}

fun Fragment.registerForStartActivityResult(): ActivityResultLauncher<ContinuationStartActivityInput> = this.registerForActivityResult(
    ContinuationStartActivityForResult()
) { result ->
    result.continuation?.resume(result.result)
}
