package com.general.projectimprovements.adapter

interface ItemProvider<T> {
    fun getItemAtPosition(position: Int): T?
    val count: Int
}
