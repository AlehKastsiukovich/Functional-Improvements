package com.general.projectimprovements.adapter.decoration

import com.general.projectimprovements.adapter.ItemProvider

abstract class HeaderCondition<T>(
    itemProvider: ItemProvider<T>
) : BaseCondition<T>(itemProvider) {

    abstract fun areItemsInTheSameGroup(current: T, previous: T): Boolean

    override fun forDrawOver(position: Int): Boolean {
        if (position == 0) {
            return true
        }

        val item = getItem(position)
        val previousItem = getItem(position - 1)

        return if (item == null || previousItem == null) {
            false
        } else {
            areItemsInTheSameGroup(item, previousItem).not()
        }
    }
}
