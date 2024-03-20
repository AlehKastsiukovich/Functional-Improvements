package com.general.projectimprovements.adapter

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BindingViewHolder<out T : ViewBinding>(open val binding: T) :
    RecyclerView.ViewHolder(binding.root)

val RecyclerView.ViewHolder.context: Context get() = itemView.context

fun RecyclerView.ViewHolder.getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(context, colorRes)

fun RecyclerView.ViewHolder.getString(@StringRes stringRes: Int) = context.getString(stringRes)
