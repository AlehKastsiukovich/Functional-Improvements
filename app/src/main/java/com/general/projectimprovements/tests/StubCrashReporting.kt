package com.general.projectimprovements.tests

class StubCrashReporting : CrashReporting {
    override fun reportNonFatalExceptions(throwable: Throwable) = Unit
    override fun log(message: String) = Unit
    override fun setCustomKeys(userId: String?, deviceId: String) = Unit
}
