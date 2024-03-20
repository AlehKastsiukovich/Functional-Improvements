package com.general.projectimprovements.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.general.projectimprovements.adapter.ItemProvider

class BottomSpaceItemDecoration<T>(
    height: Int,
    itemProvider: ItemProvider<T>,
    decorateLast: Boolean = true,
    condition: Condition = EmptyCondition()
) : ConditionItemDecoration(
    condition = if (decorateLast) {
        condition
    } else {
        NotLastItemCondition(itemProvider).and(condition)
    },
    decor = BottomSpaceDecor(height)
)

data class BottomSpaceDecor(private val height: Int) : SimpleDecor() {

    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) {
        outRect.bottom += height
    }
}
