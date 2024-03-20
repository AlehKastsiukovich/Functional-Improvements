package com.general.projectimprovements.tests

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import kotlin.test.assertTrue
import org.hamcrest.Matcher

fun <T> assertAllEquals(inputToExpected: Map<T, Any?>, testBlock: (input: T) -> Any?) {
    val failedTests = mutableListOf<Pair<Any?, Any?>>()

    inputToExpected.forEach { (input, expected) ->
        val actual = testBlock(input)
        if (expected != actual) {
            failedTests.add(expected to actual)
        }
    }
    assertTrue(
        failedTests.joinToString(
            separator = "\n",
            prefix = "\n",
            transform = { (expected, actual) -> "Expected $expected, actual $actual" }
        )
    ) { failedTests.isEmpty() }
}

fun clickItemWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            val v = view.findViewById<View>(id) as View
            v.performClick()
        }
    }
}
