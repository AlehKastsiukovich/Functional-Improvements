package com.general.projectimprovements.buildSrc

import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

fun readProperty(propertyName: String, rootDir: File): String? = readBySystemProperty(propertyName)
    ?: readBySystemProperty(propertyName.uppercase())
    ?: readBySystemEnv(propertyName)
    ?: readBySystemEnv(propertyName.uppercase())
    ?: readByGradleLocalProperties(rootDir, propertyName)
    ?: readByGradleLocalProperties(rootDir, propertyName.uppercase())

private fun readBySystemProperty(key: String): String? {
    val value = System.getProperty(key)
    val shortValue = ("$value     ").substring(0, 5)
    printPropertyValue("System.getProperty($key): $shortValue")
    return value
}

private fun readBySystemEnv(key: String): String? {
    val value = System.getenv(key)
    val shortValue = ("$value     ").substring(0, 5)
    printPropertyValue("System.getenv($key): $shortValue")
    return value
}

private fun readByGradleLocalProperties(rootDir: File, key: String): String? {
    val value = gradleLocalProperties(rootDir).getProperty(key)
    val shortValue = ("$value     ").substring(0, 5)
    printPropertyValue("GradleLocalProperties($key): $shortValue")
    return value
}

private fun printPropertyValue(value: String?) {
    val inNeedPrint = false
    if (inNeedPrint) {
        println(value)
    }
}

/**
 * Retrieve the project local properties if they are available.
 * If there is no local properties file then an empty set of properties is returned.
 */
fun gradleLocalProperties(projectRootDir : File) : Properties {
    val properties = Properties()
    val localProperties = File(projectRootDir, FN_LOCAL_PROPERTIES)

    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    }
    return properties
}