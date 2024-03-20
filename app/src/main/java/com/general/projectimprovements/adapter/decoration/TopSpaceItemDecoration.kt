package com.general.projectimprovements.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.general.projectimprovements.adapter.ItemProvider

class TopSpaceItemDecoration<T>(
    height: Int,
    itemProvider: ItemProvider<T>
) : ConditionItemDecoration(
    condition = FirstItemCondition(itemProvider),
    decor = TopSpaceDecor(height)
)

data class TopSpaceDecor(private val height: Int) : SimpleDecor() {

    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) {
        outRect.top += height
    }
}
