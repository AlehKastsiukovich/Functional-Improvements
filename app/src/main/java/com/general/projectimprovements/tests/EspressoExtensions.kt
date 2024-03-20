package com.general.projectimprovements.tests

import android.content.Context
import android.view.View
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun getInstrumentationContext(): Context = InstrumentationRegistry.getInstrumentation().context

fun hasTextInputLayoutErrorText(expectedError: String?): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description?) = Unit
    override fun matchesSafely(item: View?): Boolean = (item as? TextInputLayout)?.let { textInputLayout ->
        expectedError.orEmpty() == textInputLayout.error?.toString().orEmpty()
    } ?: false
}
