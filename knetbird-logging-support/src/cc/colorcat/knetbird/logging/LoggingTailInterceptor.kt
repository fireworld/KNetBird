package cc.colorcat.knetbird.logging

import cc.colorcat.knetbird.FileBody
import cc.colorcat.knetbird.Interceptor
import cc.colorcat.knetbird.Level
import cc.colorcat.knetbird.Response
import cc.colorcat.knetbird.internal.PairReader
import cc.colorcat.knetbird.platform.Logger
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
class LoggingTailInterceptor(
        val logger: Logger,
        val filter: TextFilter,
        val charsetIfAbsent: Charset = Charsets.UTF_8
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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
        logger.log(TAG, msg, level)
    }

    private companion object {
        const val TAG = "KNetBird"
        val LINE = buildLine(80, '-')
        val HALF_LINE = buildLine(38, '-')

        private fun buildLine(count: Int, c: Char): String = CharArray(count) { c }.let { String(it) }
    }
}
