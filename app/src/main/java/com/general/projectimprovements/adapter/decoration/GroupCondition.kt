package com.general.projectimprovements.adapter.decoration

import com.general.projectimprovements.adapter.ItemProvider

abstract class GroupCondition<T>(
    itemProvider: ItemProvider<T>
) : BaseCondition<T>(itemProvider) {

    abstract fun areItemsInTheSameGroup(current: T, next: T): Boolean

    override fun forDrawOver(position: Int): Boolean {
        val isLastItem = position == itemCount - 1
        if (isLastItem) {
            return false
        }

        val item = getItem(position)
        val nextItem = getItem(position + 1)

        return if (item == null || nextItem == null) {
            false
        } else {
            areItemsInTheSameGroup(item, nextItem)
        }
    }
}
