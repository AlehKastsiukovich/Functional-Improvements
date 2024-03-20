package com.general.projectimprovements.buildSrc

import java.io.File

open class AzureBaseService(rootDir: File) {

    private val accessToken: String? =
        readProperty(propertyName = "vstsMavenAccessToken", rootDir = rootDir)

    protected suspend fun fetchContent(apiUrl: String): ByteArray? {
        if (accessToken == null) {
            println(
                "vstsMavenAccessToken isn't provided, so it can't get Azure variables via AVO api."
            )
            return null
        }
        val client = HttpClient(CIO)
        val httpResponse: HttpResponse = client.get {
            url.takeFrom(apiUrl)
            val credentials = String(
                Base64.getEncoder()
                    .encode("$PROJECT_PREFIX$accessToken".toByteArray())
            )
            header(HttpHeaders.Authorization, "${AuthScheme.Basic} $credentials")
        }

        return if (httpResponse.status == HttpStatusCode.OK) {
            httpResponse.readBytes()
        } else {
            throw IllegalStateException(
                httpResponse.status.toString() +
                    "\n" + String(httpResponse.readBytes())
            )
        }
    }

    protected suspend fun downloadFile(downloadUrl: String, file: File, useCachedFile: Boolean = true) {
        if (useCachedFile && file.exists()) {
            return
        }
        file.parentFile.deleteRecursively()
        file.parentFile.mkdirs()
        println("downloading: $downloadUrl")
        val client = HttpClient(CIO) {
            engine { requestTimeout = 0 }
        }
        client.prepareGet {
            url(downloadUrl)
            val credentials = String(
                Base64.getEncoder()
                    .encode("$PROJECT_PREFIX$accessToken".toByteArray())
            )
            header(HttpHeaders.Authorization, "${AuthScheme.Basic} $credentials")
        }.execute { httpResponse: HttpResponse ->
            if (httpResponse.status == HttpStatusCode.OK) {
                val channel: ByteReadChannel = httpResponse.body()
                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(8 * 1024 * 1024) // 8 Mb buffer
                    if (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        file.appendBytes(bytes)
                        val fileLength = file.length().toString()
                            .reversed().chunked(3).joinToString(" ").reversed()
                        println("file length: $fileLength")
                    }
                }
            } else {
                throw IllegalStateException(
                    httpResponse.status.toString() +
                        "\n" + String(httpResponse.readBytes())
                )
            }
        }
    }

    companion object {
        private const val PROJECT_PREFIX = "trakglobal:"
    }
}