package com.general.projectimprovements.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.google.android.material.textfield.TextInputEditText

class RequiredEditText : TextInputEditText {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    private val accessibilityDelegate = object : AccessibilityDelegateCompat() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)

            if (this@RequiredEditText.text.toString().isNotEmpty()) {
                info.hintText = info.contentDescription
            } else {
                info.tooltipText = info.contentDescription
            }

            info.contentDescription = null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        ViewCompat.setAccessibilityDelegate(this, accessibilityDelegate)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        ViewCompat.setAccessibilityDelegate(this, null)
    }
}
