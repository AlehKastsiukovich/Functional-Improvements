package com.general.projectimprovements.view

import android.view.View
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AccessibleLinearLayoutManager : LinearLayoutManager {

    val recyclerView: RecyclerView

    constructor(recyclerView: RecyclerView) : super(recyclerView.context) {
        this.recyclerView = recyclerView
    }
    constructor(
        recyclerView: RecyclerView,
        @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : super(recyclerView.context, orientation, reverseLayout) {
        this.recyclerView = recyclerView
    }

    override fun onInitializeAccessibilityNodeInfo(recycler: RecyclerView.Recycler, state: RecyclerView.State, info: AccessibilityNodeInfoCompat) {
        super.onInitializeAccessibilityNodeInfo(recycler, state, info)

        val collectionInfo = AccessibilityNodeInfoCompat.CollectionInfoCompat
            .obtain(
                getRowCountForAccessibility(),
                getColumnCountForAccessibility(),
                false,
                AccessibilityNodeInfoCompat.CollectionInfoCompat.SELECTION_MODE_NONE
            )

        info.setCollectionInfo(collectionInfo)
    }

    override fun onInitializeAccessibilityNodeInfoForItem(
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State,
        host: View,
        info: AccessibilityNodeInfoCompat
    ) {
        super.onInitializeAccessibilityNodeInfoForItem(recycler, state, host, info)

        val rowIndexGuess = if (canScrollVertically()) getPosition(host) else 0
        val columnIndexGuess = if (canScrollHorizontally()) getPosition(host) else 0
        val itemInfo = AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(
            rowIndexGuess,
            1,
            columnIndexGuess,
            1,
            false,
            false
        )
        info.setCollectionItemInfo(itemInfo)
    }

    private fun getRowCountForAccessibility(): Int = if (canScrollVertically()) recyclerView.adapter?.itemCount ?: 1 else 1

    private fun getColumnCountForAccessibility(): Int = if (canScrollHorizontally()) recyclerView.adapter?.itemCount ?: 1 else 1
}
