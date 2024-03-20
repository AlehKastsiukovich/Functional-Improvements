package com.general.projectimprovements.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.properties.Delegates

@Suppress("TooManyFunctions")
class CircularProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var textPaint: Paint

    private var viewWidth = 0
    private var viewHeight = 0
    private var sweepAngle = 0f

    var startAngle: Float
        by setAndInvalidateDelegate(START_ANGLE)
    var maxAngle: Float
        by setAndInvalidateDelegate(MAX_ANGLE)

    var progress: Int
        by setAndUpdateSweepAngleDelegate(0)
    var min: Int
        by setAndUpdateSweepAngleDelegate(MIN_PROGRESS)
    var max: Int
        by setAndUpdateSweepAngleDelegate(MAX_PROGRESS)

    var animationEnabled: Boolean
        by setAndInvalidateDelegate(true)
    var animationDuration: Int
        by setAndInvalidateDelegate(ANIMATION_DURATION)
    private var progressAnimator: Animator? = null

    var progressColor: Int
        by setAndInvalidateDelegate(Color.BLACK)
    var progressBackgroundTint: Int
        by setAndInvalidateDelegate(Color.TRANSPARENT)

    var progressWidth: Float
        by setAndInvalidateDelegate(PROGRESS_WIDTH_DEFAULT)
    var cornerStyle: Paint.Cap
        by setAndInvalidateDelegate(Paint.Cap.ROUND)

    var textEnabled: Boolean
        by setAndInvalidateDelegate(true)
    var text: String
        by setAndInvalidateDelegate("")
    var textColor: Int
        by setAndInvalidateDelegate(Color.BLACK)
    var textSize: Float
        by setAndInvalidateDelegate(TEXT_SIZE_DEFAULT)

    init {
        attrs?.let { readAttrs(it) }
        isFocusable = true
    }

    private fun readAttrs(attrs: AttributeSet) {
        context.withStyledAttributes(
            set = attrs,
            attrs = R.styleable.CircularProgressBar,
            defStyleAttr = defStyleAttr
        ) {
            progress = getInt(R.styleable.CircularProgressBar_android_progress, 0)
            min = getInt(R.styleable.CircularProgressBar_android_min, MIN_PROGRESS)
            max = getInt(R.styleable.CircularProgressBar_android_max, MAX_PROGRESS)

            animationEnabled =
                getBoolean(R.styleable.CircularProgressBar_animationEnabled, true)
            animationDuration =
                getInt(R.styleable.CircularProgressBar_animationDuration, ANIMATION_DURATION)

            progressColor =
                getColor(R.styleable.CircularProgressBar_android_progressTint, Color.BLACK)
            progressBackgroundTint =
                getColor(R.styleable.CircularProgressBar_progressBackgroundTint, Color.TRANSPARENT)
            progressWidth = getDimensionPixelSize(
                R.styleable.CircularProgressBar_strokeWidth,
                PROGRESS_WIDTH_DEFAULT.toInt()
            ).toFloat()
            cornerStyle =
                getInt(R.styleable.CircularProgressBar_cornerStyle, 0).toPaintCap()

            startAngle = getFloat(
                R.styleable.CircularProgressBar_startAngle,
                START_ANGLE
            )
            maxAngle = getFloat(
                R.styleable.CircularProgressBar_maxAngle,
                MAX_ANGLE
            )

            textEnabled = getBoolean(R.styleable.CircularProgressBar_textEnabled, true)
            text = getString(R.styleable.CircularProgressBar_android_text).orEmpty()
            textColor = getColor(R.styleable.CircularProgressBar_android_textColor, Color.BLACK)
            textSize = getDimensionPixelSize(
                R.styleable.CircularProgressBar_android_textSize,
                TEXT_SIZE_DEFAULT.toInt()
            ).toFloat()
            val textAppearance =
                getResourceId(R.styleable.CircularProgressBar_android_textAppearance, 0)
            textPaint = TextView(context).apply {
                setTextAppearance(textAppearance)
                if (hasValue(R.styleable.CircularProgressBar_android_textSize)) {
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, this@CircularProgressBar.textSize)
                }
            }.paint
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackgroundArc(canvas)
        drawProgressArc(canvas)
        if (textEnabled) {
            drawText(canvas)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            viewWidth = width
            viewHeight = height
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val width = measureDimension(desiredWidth, widthMeasureSpec)
        val height = measureDimension(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)

        viewWidth = width
        viewHeight = height
    }

    private fun drawBackgroundArc(canvas: Canvas) {
        drawOutlineArc(canvas, maxAngle, progressBackgroundTint)
    }

    private fun drawProgressArc(canvas: Canvas) {
        drawOutlineArc(canvas, sweepAngle, progressColor)
    }

    private fun drawOutlineArc(canvas: Canvas, sweepAngle: Float, strokeColor: Int) {
        val outBorder = progressWidth / 2
        val (startPadding, endPadding) = if (isPaddingRelative) {
            paddingStart to paddingEnd
        } else {
            paddingLeft to paddingRight
        }
        val width = viewWidth - startPadding - endPadding
        val height = viewHeight - paddingTop - paddingBottom
        val diameter = min(width, height) - outBorder
        val outerOval = RectF(
            outBorder + startPadding,
            outBorder + paddingTop,
            diameter + startPadding,
            diameter + paddingTop
        )

        arcPaint.apply {
            color = strokeColor
            strokeWidth = progressWidth
            isAntiAlias = true
            strokeCap = cornerStyle
            style = Paint.Style.STROKE
        }
        canvas.drawArc(outerOval, startAngle, sweepAngle, false, arcPaint)
    }

    private fun drawText(canvas: Canvas) {
        val xCenterPosition = canvas.width / 2f
        val yCenterPosition =
            (canvas.height - (textPaint.ascent() + textPaint.descent())) / 2f

        textPaint.apply {
            color = textColor
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(text, xCenterPosition, yCenterPosition, textPaint)
    }

    private fun setSweepAngle(progress: Int) {
        val newAngle = calculateSweepAngleFromProgress(progress)
        progressAnimator?.cancel()
        if (animationEnabled) {
            animate(newAngle)
        } else {
            sweepAngle = newAngle
        }
        invalidate()
    }

    private fun calculateSweepAngleFromProgress(progress: Int): Float {
        val range = max.absoluteValue - min.absoluteValue
        return if (range != 0) (maxAngle / range * progress) else 0f
    }

    private fun animate(angle: Float) {
        progressAnimator = ValueAnimator.ofFloat(0f, angle).apply {
            interpolator = DecelerateInterpolator()
            duration = animationDuration.toLong()
            addUpdateListener { valueAnimator ->
                sweepAngle = valueAnimator.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun setAndUpdateSweepAngleDelegate(initialValue: Int) = Delegates.observable(initialValue) { _, old, new ->
        if (old != new) {
            setSweepAngle(progress)
        }
    }

    companion object {
        private const val START_ANGLE = -90f
        private const val MAX_ANGLE = 360f

        private const val MIN_PROGRESS = 0
        private const val MAX_PROGRESS = 100

        private const val PROGRESS_WIDTH_DEFAULT = 20f
        private const val TEXT_SIZE_DEFAULT = 17f

        private const val ANIMATION_DURATION = 400

        private fun Int.toPaintCap() = when (this) {
            0 -> Paint.Cap.BUTT
            1 -> Paint.Cap.ROUND
            2 -> Paint.Cap.SQUARE
            else -> throw IllegalArgumentException("No Paint.Cap found for value $this")
        }
    }
}
