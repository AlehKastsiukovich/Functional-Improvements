@file:Suppress("TooManyFunctions")

package com.general.projectimprovements.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.viewbinding.ViewBinding
import com.general.projectimprovements.view.CommonScoreViewState
import com.general.projectimprovements.view.DrivenDialScoreViewState
import com.general.projectimprovements.view.ScoreViewState
import com.general.projectimprovements.view.TripScoreViewState
import com.google.android.material.textfield.TextInputLayout
import java.util.MissingFormatArgumentException
import kotlin.math.abs
import kotlin.properties.Delegates

val <T : ViewBinding> T.context: Context get() = root.context

// region View extensions

fun <T> View.setAndInvalidateDelegate(initialValue: T) = Delegates.observable(initialValue) { _, old, new ->
    if (old != new) invalidate()
}

fun View.showKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService<InputMethodManager>()
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setFocus() {
    requestFocus()
    showKeyboard()
}

fun View.resetFocus() {
    clearFocus()
    hideKeyboard()
}

fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPadding(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        block(view, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}

private fun recordInitialPadding(view: View) = Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.setOnExpandListener(expandableView: View, toggleView: View, rotationAngle: Float = 0f) = setOnClickListener {
    val nextState = expandableView.isVisible.not()

    toggleView.animate()
        .rotation(rotationAngle * nextState.toInt())
        .setDuration(
            context.resources.getInteger(android.R.integer.config_shortAnimTime)
                .toLong()
        )
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                expandableView.isVisible = nextState
            }
        })
        .start()
}

fun View.setFilteredOnClickListener(listener: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        @Suppress("MagicNumber")
        private val DOUBLE_CLICK_FILTER = 700L
        private var lastClickedTime = 0L
        override fun onClick(view: View?) {
            val currentTimeMillis = System.currentTimeMillis()
            if (abs(lastClickedTime - currentTimeMillis) > DOUBLE_CLICK_FILTER) {
                lastClickedTime = currentTimeMillis
                listener.invoke()
            }
        }
    })
}

fun View.hideTooltip() = setOnLongClickListener { true }

// endregion

// region TextView extensions

fun TextView.setOnDoneActionListener(action: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            action()
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }
}

fun TextView.clearFocusOnDone() {
    setOnDoneActionListener {
        resetFocus()
    }
}

fun TextView.setCompoundDrawableIds(start: Int? = 0, top: Int? = 0, end: Int? = 0, bottom: Int? = 0) =
    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(
        this,
        start ?: 0,
        top ?: 0,
        end ?: 0,
        bottom ?: 0
    )

fun TextView.setTextWithLink(text: String, link: String, onLinkClickListener: () -> Unit) {
    if (link.isNotBlank()) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onLinkClickListener()
            }
        }
        val fullText = "$text $link".trim()
        val spannableString = SpannableString(fullText).apply {
            setSpan(
                clickableSpan,
                fullText.length - link.length,
                fullText.length,
                if (text.isBlank()) {
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                } else {
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                }
            )
        }
        this.text = spannableString
        movementMethod = LinkMovementMethod.getInstance()
    } else {
        this.text = text
    }
}

fun TextView.setUnderlinedTextStyle() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.getInsideCenterOffset(@DimenRes viewHeight: Int): Int {
    val diameter = context.getPixelSize(viewHeight)
    return (measuredHeight - diameter) / 2
}

fun TextView.getOutsideCenterOffset(@DimenRes viewHeight: Int): Int {
    val diameter = context.getPixelSize(viewHeight)
    return measuredHeight - diameter / 2
}

fun TextView.setTextWithVisibility(stringText: String?) {
    isVisible = stringText.isNullOrEmpty().not()
    text = stringText
}

inline fun TextView.changeSubtitleState(
    androidService: AndroidService,
    crashReporting: CrashReporting,
    state: SubtitleViewState?,
    crossinline call: (link: String?) -> Unit
) {
    state?.body?.let { textKey ->
        isVisible = true
        if (state.links.isNullOrEmpty()) {
            text = androidService.getStringByKeyOrEmpty(textKey)
        } else {
            makeLinks(
                stringBody = try {
                    androidService.getStringWithArg(
                        key = textKey,
                        arg = state.links.map {
                            androidService.getStringByKeyOrEmpty(it.label)
                        }.toTypedArray()
                    )
                } catch (error: MissingFormatArgumentException) {
                    crashReporting.reportNonFatalExceptions(error)
                    androidService.getStringByKey(key = textKey)
                },
                links = state.links.map {
                    Pair(
                        androidService.getStringByKeyOrEmpty(it.label)
                    ) {
                        call(it.link)
                    }
                }.toTypedArray()
            )
        }
    }
}

// endregion

// region ProgressBar extensions

fun CircularProgressBar.setProgress(score: ScoreViewState?, setColorLevel: Boolean = true, setContentDescription: Boolean = true) {
    progress = score?.progress?.toInt() ?: 0
    text = score?.value.scoreValueFormat(context)
    if (setContentDescription) {
        val description = when (score) {
            is CommonScoreViewState, is DrivenDialScoreViewState -> text
            is TripScoreViewState ->
                score.accessibilityLabel
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { "$it $text" } ?: text
            null -> text
        }

        contentDescription = description
    } else {
        contentDescription = null
        isFocusable = false
    }
    if (setColorLevel) {
        progressColor = context.getColor(score?.type.colorRes)
    }
}

// endregion

fun TextInputLayout.setErrorState(isError: Boolean) {
    error = if (isError) " " else null
    getChildAt(1)?.isVisible = false
}

fun ImageView.setImageByKey(imageKey: String?, androidService: AndroidService) {
    imageKey
        ?.let { key -> androidService.getDrawableIdByKey(key) }
        ?.let { resId -> setImageResource(resId) }
}
