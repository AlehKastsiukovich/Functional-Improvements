package com.general.projectimprovements.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

class PagingLoadStateAdapter(
    private val retry: (() -> Unit)?
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PagingLoadStateViewHolder = PagingLoadStateViewHolder(
        context = parent.context,
        retry = retry
    ).apply {
        bind(loadState)
    }
}

class PagingLoadStateViewHolder(
    context: Context,
    private val retry: (() -> Unit)?
) : RecyclerView.ViewHolder(ComposeView(context)) {
    private val stateItemsHeight = 60.dp

    fun bind(loadState: LoadState) {
        (itemView as ComposeView).setContent {
            if (loadState is LoadState.Loading) {
                OneAppPaginationProgress(
                    itemHeight = stateItemsHeight
                )
            } else {
                if (retry != null) {
                    OneAppRetryPaginationButton(
                        text = getString(CustomerR.string.GENERAL_RETRY),
                        itemHeight = stateItemsHeight,
                        retry = { retry.invoke() }
                    )
                }
            }
        }
    }
}
