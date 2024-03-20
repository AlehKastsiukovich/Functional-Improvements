package com.intellimec.oneapp.common.view

import android.view.View
import kotlin.math.min

fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
    val mode = View.MeasureSpec.getMode(measureSpec)
    val size = View.MeasureSpec.getSize(measureSpec)

    return when (mode) {
        View.MeasureSpec.EXACTLY -> size
        View.MeasureSpec.AT_MOST -> min(desiredSize, size)
        else -> desiredSize
    }
}
