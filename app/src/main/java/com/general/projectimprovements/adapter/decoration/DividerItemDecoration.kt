package com.general.projectimprovements.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.graphics.toRectF
import androidx.recyclerview.widget.RecyclerView
import com.general.projectimprovements.adapter.ItemProvider

class DividerItemDecoration<T>(
    context: Context,
    itemProvider: ItemProvider<T>,
    @DimenRes height: Int = R.dimen.list_item_divider_height,
    @ColorRes color: Int = R.color.ui_kit_color_background,
    decorateLast: Boolean = false,
    condition: Condition = EmptyCondition()
) : ConditionItemDecoration(
    condition = if (decorateLast) {
        condition
    } else {
        NotLastItemCondition(itemProvider).and(condition)
    },
    decor = DividerDecor(
        height =
        color = context.getColor(color)
    )
)

data class DividerDecor(private val height: Int, private val color: Int) : SimpleDecor() {

    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) {
        outRect.bottom += height
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, view: View, position: Int, state: RecyclerView.State) {
        val viewBounds = getViewBounds(parent, view).toRectF()
        val paint = Paint().apply {
            isAntiAlias = true
            color = this@DividerDecor.color
        }
        canvas.drawRect(
            viewBounds.left,
            viewBounds.bottom,
            viewBounds.right,
            (viewBounds.bottom + height),
            paint
        )
    }
}
