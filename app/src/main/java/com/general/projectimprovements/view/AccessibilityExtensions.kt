package com.general.projectimprovements.view

import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate

fun View.setImportantForAccessibility(isImportant: Boolean) {
    importantForAccessibility = if (isImportant) {
        View.IMPORTANT_FOR_ACCESSIBILITY_YES
    } else {
        View.IMPORTANT_FOR_ACCESSIBILITY_NO
    }
}

fun RecyclerView.setEmptyAccessibilityDelegate() {
    setAccessibilityDelegateCompat(object : RecyclerViewAccessibilityDelegate(this) {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(host, info)

            AccessibilityNodeInfoCompat.CollectionInfoCompat
                .obtain(0, 0, false, 0)
                .apply { info.setCollectionInfo(this) }
        }
    })
}

fun TextView.setButtonAccessibilityDelegate() {
    ViewCompat.setAccessibilityDelegate(
        this,
        object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                super.onInitializeAccessibilityNodeInfo(host, info)

                info.className = Button::class.java.name
            }
        }
    )
}

fun TextView.setHeadingAccessibilityDelegate() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        isAccessibilityHeading = true
    } else {
        ViewCompat.setAccessibilityDelegate(
            this,
            object : AccessibilityDelegateCompat() {
                override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    info.isHeading = true
                }
            }
        )
    }
}
