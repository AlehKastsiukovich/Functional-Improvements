package com.general.projectimprovements.tests

import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun Fragment.setFragmentBackground(@ColorRes colorId: Int? = null) {
    view?.setBackgroundColor(
        resources.getColor(
            colorId ?: android.R.color.background_light,
            null
        )
    )
}

fun FragmentActivity.setActivityBackground(@ColorRes colorId: Int? = null) {
    window.decorView.setBackgroundColor(
        resources.getColor(
            colorId ?: android.R.color.background_light,
            null
        )
    )
}
