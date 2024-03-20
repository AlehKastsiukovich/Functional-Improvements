package com.general.projectimprovements.view

import android.view.Window

/**
 * An interface used to setup window and system bars appearance.
 * Should be implemented in the activity. To setup a specific appearance pass
 * a [WindowController] object to the fragment and call method [setup]
 * in [androidx.fragment.app.Fragment.onActivityCreated] method.
 */
interface WindowController {
    fun setup(showToolbar: Boolean, block: (Window) -> Unit = {})

    fun setupNoAppBars(block: (Window) -> Unit = {}) = setup(showToolbar = false, block = block)

    fun setToolbarTitle(title: String)

    fun enterInFullScreenMode()

    fun exitFromFullScreenMode()

    fun updateBottomBarElements()
}
