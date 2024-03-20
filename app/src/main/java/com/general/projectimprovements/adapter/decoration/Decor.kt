package com.general.projectimprovements.adapter.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface Decor {

    fun prepareDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)

    fun onDrawOver(canvas: Canvas, parent: RecyclerView, view: View, position: Int, state: RecyclerView.State)

    fun onPostDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)

    fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int)

    fun getItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int, state: RecyclerView.State)
}

open class SimpleDecor : Decor {
    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) = Unit

    override fun getItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int, state: RecyclerView.State) = Unit

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, view: View, position: Int, state: RecyclerView.State) = Unit

    override fun onPostDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) = Unit

    override fun prepareDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) = Unit

    protected fun getViewBounds(parent: ViewGroup, child: View): Rect = Rect().also { viewBounds ->
        child.getDrawingRect(viewBounds)
        parent.offsetDescendantRectToMyCoords(child, viewBounds)
    }
}
