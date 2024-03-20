package com.general.projectimprovements.view

import android.content.Context
import android.util.AttributeSet

class RequiredConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isRequired: Boolean = false

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val state = super.onCreateDrawableState(extraSpace + 1)
        if (isRequired) {
            mergeDrawableStates(state, REQUIRED_STATE)
        }
        return state
    }

    fun setRequired(required: Boolean) {
        if (isRequired != required) {
            isRequired = required
            refreshDrawableState()
        }
    }

    companion object {
        private val REQUIRED_STATE = intArrayOf(UiCommonR.attr.state_required)
    }
}
