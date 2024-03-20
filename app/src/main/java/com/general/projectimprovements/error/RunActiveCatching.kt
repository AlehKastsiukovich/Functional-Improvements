package com.general.projectimprovements.error

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive

@Suppress("TooGenericExceptionCaught")
inline fun CoroutineScope.runActiveCatching(work: () -> Unit, handler: (t: Throwable) -> Unit) {
    try {
        work()
    } catch (t: Throwable) {
        if (isActive) {
            handler(t)
        }
    }
}
