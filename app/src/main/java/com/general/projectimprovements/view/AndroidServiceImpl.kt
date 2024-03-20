package com.general.projectimprovements.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

@Suppress("TooManyFunctions")
class AndroidServiceImpl(
    private val context: Context
) : AndroidService {

    private val resources get() = context.resources

    override val appName get() = context.getString(R.string.APP_NAME)

    override val launcherAppName get() =
        context.getString(R.string.APP_NAME_LAUNCHER)

    override fun getStringByKey(key: String): String {
        val stringResId = resources.getIdentifier(key, DEF_TYPE_STRING, context.packageName)
        return if (stringResId == 0) key else context.getString(stringResId)
    }

    override fun getStringByKeyOrDefault(key: String?, default: Int?): String =
        key?.let { getStringByKey(it) } ?: default?.let { context.getString(it) }.orEmpty()

    override fun getStringByKeyOrEmpty(key: String?): String = key?.let { getStringByKey(it) }.orEmpty()

    override fun getStringByKeyOrNull(key: String?): String? {
        val stringResId = resources.getIdentifier(key, DEF_TYPE_STRING, context.packageName)
        return if (stringResId == 0) null else context.getString(stringResId)
    }

    override fun getStringWithArg(key: String?, vararg arg: String?): String = String.format(getStringByKeyOrEmpty(key), *arg)

    override fun getStringWithAppNameArg(key: String?): String = String.format(getStringByKeyOrEmpty(key), appName)

    override fun getStringWithLauncherAppNameArg(key: String?): String = String.format(getStringByKeyOrEmpty(key), launcherAppName)

    @DrawableRes
    override fun getDrawableIdByKey(key: String): Int = resources.getIdentifier(key, DEF_TYPE_DRAWABLE, context.packageName)

    @Suppress("TooGenericExceptionCaught")
    override fun getBitmapByKey(key: String?): Bitmap? = try {
        key
            ?.let { getDrawableIdByKey(it) }
            ?.let { resId -> getBitmapByResId(resId) }
    } catch (t: Throwable) {
        reportNonFatalExceptions(t)
        null
    }

    override fun getBitmapByResId(@DrawableRes resId: Int): Bitmap? = context.getDrawableCompat(resId)
        ?.let { drawable -> getBitmapFromDrawable(drawable) }

    override fun getBitmapFromDrawable(drawable: Drawable): Bitmap = drawable.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            .apply { draw(Canvas(this)) }
    }

    companion object {
        private const val DEF_TYPE_STRING = "string"
        private const val DEF_TYPE_PLURALS = "plurals"
        private const val DEF_TYPE_DRAWABLE = "drawable"
    }
}
