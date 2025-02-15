package com.general.projectimprovements.tests

const val FLAKY_TEST_DEFAULT_RETRY_COUNT = 7

fun <R> retryFlaky(tryCount: Int = FLAKY_TEST_DEFAULT_RETRY_COUNT, block: () -> R): R {
    var tryCounter = 1
    while (true) {
        try {
            return block()
        } catch (e: Throwable) {
            val isLastTry = tryCounter == tryCount
            if (isLastTry) {
                throw e
            }
        }
        tryCounter++
        Thread.sleep(100)
    }
}
