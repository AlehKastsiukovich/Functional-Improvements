package com.general.projectimprovements.tests

class StubDurationFormat : DurationFormat {
    override fun format(seconds: Int): String = seconds.toString()
}
