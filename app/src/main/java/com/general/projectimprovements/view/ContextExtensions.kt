package com.general.projectimprovements.view

import android.content.Context
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.core.content.ContextCompat
import java.util.Locale

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

fun Context.getQuantityString(@PluralsRes resId: Int, quantity: Int) = resources.getQuantityString(resId, quantity, quantity)

fun Context.getPixelSize(@DimenRes resId: Int): Int = resources.getDimensionPixelSize(resId)

fun Context.getPixelSizeF(@DimenRes resId: Int): Float = getPixelSize(resId).toFloat()

fun Context.getCurrentLocale(): Locale = resources.configuration.locales[0]

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, duration).show()
