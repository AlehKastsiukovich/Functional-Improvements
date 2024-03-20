package com.general.projectimprovements.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.accessibility.AccessibilityEventCompat

class EditCompatImageView : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    @DrawableRes
    private var alternativeStateRes: Int? = null

    @DrawableRes
    private var defaultStateRes: Int? = null

    private var alternativeContentDescription: CharSequence? = null
    private var defaultContentDescription: CharSequence? = null

    fun changeState(isInEditState: Boolean) {
        contentDescription = if (isInEditState) {
            alternativeStateRes?.let {
                setImageResource(it)
            }
            alternativeContentDescription
        } else {
            defaultStateRes?.let {
                setImageResource(it)
            }
            defaultContentDescription
        }

        sendAccessibilityEvent(AccessibilityEventCompat.CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)

        defaultStateRes = resId
    }

    override fun setContentDescription(contentDescription: CharSequence?) {
        super.setContentDescription(contentDescription)

        defaultContentDescription = contentDescription
    }

    fun setAlternativeImageResources(@DrawableRes resId: Int) {
        alternativeStateRes = resId
    }

    fun setAlternativeContentDescription(contentDescription: CharSequence?) {
        alternativeContentDescription = contentDescription
    }
}
