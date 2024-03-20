package com.general.projectimprovements.tests

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import com.github.takahirom.roborazzi.captureRoboImage

fun captureComposableScreenshot(name: String, content: @Composable () -> Unit) {
    captureRoboImage(
        filePath = "screenshots/${BuildConfig.FLAVOR}/$name.png",
        content = content
    )
}

fun captureFragmentScreenshot(fragment: Fragment, name: String) {
    fragment.requireView().captureRoboImage(
        filePath = "screenshots/${BuildConfig.FLAVOR}/$name.png"
    )
}

fun captureActivityScreenshot(activity: Activity, name: String) {
    activity.window.decorView.rootView.captureRoboImage(
        filePath = "screenshots/${BuildConfig.FLAVOR}/$name.png"
    )
}
fun captureDialogScreenshot(dialog: Dialog, name: String) {
    requireNotNull(dialog.window).decorView.captureRoboImage(
        filePath = "screenshots/${BuildConfig.FLAVOR}/$name.png"
    )
}

fun captureCustomViewScreenshot(
    name: String,
    @StyleRes themeResId: Int,
    createView: (
        fragment: Fragment,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) -> View
) {
    val scenario = launchFragmentInContainer<Fragment>(
        factory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment = CustomViewFragment(createView = createView)
        },
        themeResId = themeResId
    )
    scenario.moveToState(Lifecycle.State.RESUMED)
    scenario.onFragment { fragment ->
        fragment.setFragmentBackground()
        requireNotNull(fragment.view).captureRoboImage(
            filePath = "screenshots/${BuildConfig.FLAVOR}/$name.png"
        )
    }
}

internal class CustomViewFragment(
    private val createView: (
        fragment: Fragment,
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) -> View
) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FrameLayout(inflater.context).also {
            it.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.addView(createView(this, inflater, container, savedInstanceState))
        }
}
