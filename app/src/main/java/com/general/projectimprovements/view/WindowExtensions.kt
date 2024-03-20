@file:Suppress("TooManyFunctions")

package com.general.projectimprovements.view

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi

fun Window.setDefault() {
    setDefaultSystemBarsInsets()
    setBackgroundColor(R.color.ui_kit_color_background)
    setupStatusBarColor(StatusBarColor.PRIMARY)
}

fun Window.setupStatusBarColor(color: StatusBarColor) {
    statusBarColor = context.getColor(color.colorValue)
}

fun Window.setBackgroundColor(@ColorRes colorRes: Int) {
    setBackgroundDrawable(ColorDrawable(context.getColor(colorRes)))
}

fun Window.setFullscreenWithLightBars() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setFullscreenWithLightBarsApi30()
    } else {
        setFullscreenWithLightBarsApiPre30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.setFullscreenWithLightBarsApi30() {
    setFullscreenBarApi30()
    insetsController?.setSystemBarsAppearance(
        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
    )
}

@Suppress("Deprecation")
private fun Window.setFullscreenWithLightBarsApiPre30() {
    decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        )
}

fun Window.setFullscreenBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setFullscreenBarApi30()
    } else {
        setFullScreenBarApiPre30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.setFullscreenBarApi30() {
    setDecorFitsSystemWindows(false)
    insetsController?.apply {
        show(WindowInsets.Type.navigationBars())
    }
}

@Suppress("Deprecation")
private fun Window.setFullScreenBarApiPre30() {
    decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
}

fun Window.hideScreenBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        hideFullscreenBarApi30()
    } else {
        hideFullscreenBarApiPre30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.hideFullscreenBarApi30() {
    insetsController?.hide(WindowInsets.Type.statusBars())
}

@Suppress("Deprecation")
private fun Window.hideFullscreenBarApiPre30() {
    decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
}

fun Window.showFullScreenPlayer() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        showFullScreenPlayerApi30()
    } else {
        showFullScreenPlayerApiPre30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.showFullScreenPlayerApi30() {
    setDecorFitsSystemWindows(false)
    insetsController?.apply {
        show(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
    }
}

@Suppress("Deprecation")
private fun Window.showFullScreenPlayerApiPre30() {
    decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
}

fun Window.setDefaultSystemBarsInsets() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDefaultInsetsApi30()
    } else {
        setDefaultInsetsApiPre30()
    }
}

@RequiresApi(Build.VERSION_CODES.R)
private fun Window.setDefaultInsetsApi30() {
    setDecorFitsSystemWindows(true)
    insetsController?.apply {
        show(WindowInsets.Type.navigationBars() or WindowInsets.Type.statusBars())
        setSystemBarsAppearance(0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
    }
}

@Suppress("Deprecation")
private fun Window.setDefaultInsetsApiPre30() {
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}
