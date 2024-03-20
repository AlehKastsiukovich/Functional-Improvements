package com.general.projectimprovements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding

abstract class BindingListAdapter<T, B : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BindingViewHolder<B>>(diffCallback), ItemProvider<T> {

    override val count: Int
        get() = itemCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<B> {
        val inflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(inflater, parent, viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<B>

    override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int) {
        val item = getItem(position)
        onBindViewHolder(holder, item)
    }

    abstract fun onBindViewHolder(holder: BindingViewHolder<B>, item: T)

    override fun getItemAtPosition(position: Int): T = super.getItem(position)
}
