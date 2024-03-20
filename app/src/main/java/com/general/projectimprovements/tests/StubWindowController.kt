package com.general.projectimprovements.tests

import android.view.Window
import com.general.projectimprovements.view.WindowController

val stubWindowController = object : WindowController {
    override fun setup(showToolbar: Boolean, block: (Window) -> Unit) = Unit

    override fun setToolbarTitle(title: String) = Unit

    override fun enterInFullScreenMode() = Unit

    override fun exitFromFullScreenMode() = Unit

    override fun updateBottomBarElements() = Unit
}
