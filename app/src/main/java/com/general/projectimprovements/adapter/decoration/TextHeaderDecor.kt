package com.general.projectimprovements.adapter.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import com.intellimec.oneapp.common.getPixelSize

class TextHeaderDecor(
    context: Context,
    private val textProvider: HeaderProvider,
    @ColorRes private val textColor: Int = color.ui_kit_color_bodyText,
    @StyleRes private val textAppearance: Int = R.style.TextAppearance_OneApp_Body
) : SimpleDecor() {

    private val textPaint: TextPaint

    private var decorHeight: Int

    init {
        textPaint = createTextPaint(context)
        decorHeight = getDecorHeight(context)
    }

    override fun getConditionItemOffsets(parent: RecyclerView, outRect: Rect, view: View, position: Int) {
        outRect.top += decorHeight
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, view: View, position: Int, state: RecyclerView.State) {
        val viewBounds = getViewBounds(parent, view)
        val yPosition =
            (viewBounds.top - decorHeight / 2f) - (textPaint.descent() + textPaint.ascent()) / 2f
        var xPosition = 0f

        textProvider.getHeaders(position).forEach { header ->
            when (header.align) {
                TextAlign.LEFT -> {
                    textPaint.textAlign = Paint.Align.LEFT
                    xPosition = viewBounds.left + header.padding
                }
                TextAlign.CENTER -> {
                    textPaint.textAlign = Paint.Align.CENTER
                    xPosition = (viewBounds.right - viewBounds.left) / 2f
                }
                TextAlign.RIGHT -> {
                    textPaint.textAlign = Paint.Align.RIGHT
                    xPosition = viewBounds.right - header.padding
                }
            }
            canvas.drawText(header.text, xPosition, yPosition, textPaint)
        }
    }

    private fun createTextPaint(context: Context): TextPaint = TextView(context)
        .apply { setTextAppearance(textAppearance) }
        .paint
        .apply { color = context.getColor(textColor) }

    private fun getDecorHeight(context: Context): Int {
        val verticalPadding = context.getPixelSize(R.dimen.padding_medium)
        return (verticalPadding * 2 + textPaint.descent() - textPaint.ascent()).toInt()
    }

    interface HeaderProvider {
        fun getHeaders(position: Int): List<Header>
    }

    data class Header(
        val text: String,
        val align: TextAlign = TextAlign.LEFT,
        val padding: Float = 0f
    )

    enum class TextAlign {
        LEFT,
        CENTER,
        RIGHT
    }
}
