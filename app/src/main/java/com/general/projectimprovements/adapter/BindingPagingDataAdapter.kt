package com.general.projectimprovements.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

abstract class BindingPagingDataAdapter<T : Any, B : ViewBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, BindingViewHolder<B>>(diffCallback), ItemProvider<T> {

    override val count: Int get() = itemCount

    val isEmpty: Boolean get() = itemCount == 0

    override fun getItemAtPosition(position: Int): T? = super.getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<B> {
        val inflater = LayoutInflater.from(parent.context)
        return onCreateViewHolder(inflater, parent, viewType)
    }

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): BindingViewHolder<B>

    override fun onBindViewHolder(holder: BindingViewHolder<B>, position: Int) {
        getItem(position)?.let {
            onBindViewHolder(holder, it)
        }
    }

    abstract fun onBindViewHolder(holder: BindingViewHolder<B>, item: T)
}

val CombinedLoadStates.isLoading get() = source.refresh is LoadState.Loading
val CombinedLoadStates.isError get() = source.refresh is LoadState.Error
val CombinedLoadStates.isLoadingWhilePaging get() = source.append is LoadState.Loading
val CombinedLoadStates.isErrorWhilePaging get() = source.append is LoadState.Error
val CombinedLoadStates.isEndReached get() = source.append.endOfPaginationReached
