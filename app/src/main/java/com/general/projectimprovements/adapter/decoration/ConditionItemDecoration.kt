package com.general.projectimprovements.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class ConditionItemDecoration(
    private val condition: Condition,
    private val decor: Decor
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (condition.isForDrawOver(position)) {
            decor.getConditionItemOffsets(parent, outRect, view, position)
        }
        decor.getItemOffsets(parent, outRect, view, position, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        decor.prepareDrawOver(c, parent, state)

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (condition.isForDrawOver(position)) {
                decor.onDrawOver(c, parent, child, position, state)
            }
        }

        decor.onPostDrawOver(c, parent, state)
    }
}
