package com.general.projectimprovements.adapter.decoration

import com.general.projectimprovements.adapter.ItemProvider

interface Condition {
    fun isForDrawOver(position: Int): Boolean

    fun and(other: Condition): Condition = Builder(this).and(other)

    fun or(other: Condition): Condition = Builder(this).or(other)

    private data class Builder(private val condition: Condition = EmptyCondition()) {

        fun and(other: Condition) = object : Condition {
            override fun isForDrawOver(position: Int): Boolean {
                return condition.isForDrawOver(position).and(other.isForDrawOver(position))
            }
        }

        fun or(other: Condition) = object : Condition {
            override fun isForDrawOver(position: Int): Boolean {
                return condition.isForDrawOver(position).or(other.isForDrawOver(position))
            }
        }
    }
}

class EmptyCondition : Condition {
    override fun isForDrawOver(position: Int) = true
}

abstract class BaseCondition<T>(
    private val itemProvider: ItemProvider<T>
) : Condition {

    protected val itemCount get() = itemProvider.count

    protected fun getItem(position: Int) = itemProvider.getItemAtPosition(position)

    private fun isValidPosition(position: Int) = position in 0 until itemCount && itemCount > 0

    abstract fun forDrawOver(position: Int): Boolean

    override fun isForDrawOver(position: Int): Boolean {
        if (isValidPosition(position).not()) {
            return false
        }
        return forDrawOver(position)
    }
}

class FirstItemCondition<T>(itemProvider: ItemProvider<T>) : BaseCondition<T>(itemProvider) {
    override fun forDrawOver(position: Int) = position == 0
}

class LastItemCondition<T>(itemProvider: ItemProvider<T>) : BaseCondition<T>(itemProvider) {
    override fun forDrawOver(position: Int) = position == itemCount - 1
}

class NotLastItemCondition<T>(itemProvider: ItemProvider<T>) : BaseCondition<T>(itemProvider) {
    override fun forDrawOver(position: Int) = position != itemCount - 1
}
