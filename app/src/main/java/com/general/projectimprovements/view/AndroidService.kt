package com.general.projectimprovements.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

@Suppress("TooManyFunctions")
interface AndroidService {
    val appName: String
    val launcherAppName: String

    fun getStringByKey(key: String): String
    fun getStringByKeyOrDefault(key: String?, @StringRes default: Int? = null): String
    fun getStringByKeyOrEmpty(key: String?): String
    fun getStringByKeyOrNull(key: String?): String?
    fun getStringWithArg(key: String?, vararg arg: String?): String
    fun getStringWithAppNameArg(key: String?): String
    fun getStringWithLauncherAppNameArg(key: String?): String

    @DrawableRes
    fun getDrawableIdByKey(key: String): Int

    fun getBitmapByKey(key: String?): Bitmap?
    fun getBitmapByResId(@DrawableRes resId: Int): Bitmap?
    fun getBitmapFromDrawable(drawable: Drawable): Bitmap
}
