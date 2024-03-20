package com.general.projectimprovements.tests

import android.icu.text.NumberFormat

class StubDistanceFormat : DistanceFormat {
    override fun format(distance: Distance, formatWidth: MeasureFormatWidth): String = "$distance $formatWidth"

    override fun customFormat(distance: Distance, numberFormat: NumberFormat, formatWidth: MeasureFormatWidth): String = "$distance $formatWidth"
}
