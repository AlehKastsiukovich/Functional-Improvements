package com.general.projectimprovements.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class HorizontalAdaptiveSpacingItemDecoration(
    private val percent: Float
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val spacing = (parent.width * percent / 2).roundToInt()

        outRect.left = if (position == 0) 0 else spacing
        outRect.right = if (position == state.itemCount - 1) 0 else spacing
    }
}
