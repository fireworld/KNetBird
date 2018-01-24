package cc.colorcat.knetbird.logging

import cc.colorcat.knetbird.*
import cc.colorcat.knetbird.internal.PairReader
import cc.colorcat.knetbird.platform.Platform
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
class LoggingTailInterceptor(
        private val filter: ContentFilter = object : ContentFilter {},
        private val charsetIfAbsent: Charset = Charsets.UTF_8
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request
        var response = chain.proceed(request)

        synchronized(LoggingTailInterceptor.TAG) {
            log(HALF_LINE + request.method.name + HALF_LINE, Level.DEBUG)
            log("request url = ${request.url}", Level.DEBUG)
            logPair("request header", request.headers, Level.DEBUG)
            if (request.method.needBody()) {
                logPair("request parameter", request.parameters, Level.DEBUG)
                logFiles(request.fileBodies, Level.DEBUG)
            }

            log("response --> ${response.code} -- ${response.msg}", Level.INFO)
            logPair("response header", response.headers, Level.INFO)
            response.responseBody?.also {
                it.contentType()?.apply {
                    if (filter.filter(this)) {
                        val bytes = it.bytes()
                        val charset = it.charset() ?: charsetIfAbsent
                        val content = String(bytes, charset)
                        log("response content --> $content", Level.INFO)
                        val newBody = ResponseBody.create(bytes, this, charset)
                        response = response.newBuilder().responseBody(newBody).build()
                    }
                }
            }
            log(LINE, Level.INFO)
        }
        return response
    }

    private companion object {
        const val TAG = "KNetBird"
        const val LINE = "--------------------------------------------------------------------------------"
        const val HALF_LINE = "--------------------------------------"

        private fun logPair(type: String, pair: PairReader, level: Level) {
            for ((name, value) in pair) {
                log("$type --> $name = $value", level)
            }
        }

        private fun logFiles(fileBodies: List<FileBody>, level: Level) {
            for (body in fileBodies) {
                log("request file --> $body", level)
            }
        }

        private fun log(msg: String, level: Level) {
            Platform.get().logger.log(TAG, msg, level)
        }
    }
}
