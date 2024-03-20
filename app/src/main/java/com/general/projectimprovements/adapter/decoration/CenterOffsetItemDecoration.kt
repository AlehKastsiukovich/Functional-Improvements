package com.general.projectimprovements.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.general.projectimprovements.adapter.ItemProvider

class CenterFirstItemDecoration<T>(itemProvider: ItemProvider<T>) : ConditionItemDecoration(
    condition = FirstItemCondition(itemProvider),
    decor = CenterLeftOffsetDecor()
)

class CenterLastItemDecoration<T>(itemProvider: ItemProvider<T>) : ConditionItemDecoration(
    condition = LastItemCondition(itemProvider),
    decor = CenterRightOffsetDecor()
)

abstract class CenterHorizontalOffsetDecor : SimpleDecor() {

    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) {
        val itemWidth = view.layoutParams.width
        val offset = (parent.width - itemWidth) / 2

        setItemOffsets(outRect, offset)
    }

    abstract fun setItemOffsets(outRect: Rect, offset: Int)
}

class CenterLeftOffsetDecor : CenterHorizontalOffsetDecor() {

    override fun setItemOffsets(outRect: Rect, offset: Int) {
        outRect.left = offset
    }
}

class CenterRightOffsetDecor : CenterHorizontalOffsetDecor() {

    override fun setItemOffsets(outRect: Rect, offset: Int) {
        outRect.right = offset
    }
}
