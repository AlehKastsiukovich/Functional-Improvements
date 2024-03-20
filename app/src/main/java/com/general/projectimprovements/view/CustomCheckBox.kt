package com.general.projectimprovements.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button

class CustomCheckBox : AppCompatCheckBox {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
        super(context, attributeSet, defStyleAttr)

    var customStateDescription: Pair<String, String>? = null
        set(value) {
            field = value

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                stateDescription = ""
            }
        }

    @Override
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)

        if (customStateDescription != null) {
            info.isCheckable = false
            info.contentDescription = "$contentDescription, ${getCustomStateDescription()}"
        }
    }

    @Override
    override fun setChecked(checked: Boolean) {
        val isNeedToUpdate = checked != isChecked
        super.setChecked(checked)

        if (customStateDescription != null && isNeedToUpdate) {
            announceForAccessibility(getCustomStateDescription())
        }
    }

    private fun getCustomStateDescription(): String? {
        return customStateDescription?.run {
            if (isChecked) {
                second
            } else {
                first
            }
        }
    }

    override fun getAccessibilityClassName(): CharSequence = Button::class.java.name
}
