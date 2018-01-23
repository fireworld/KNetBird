package cc.colorcat.knetbird.logging

import cc.colorcat.knetbird.Interceptor
import cc.colorcat.knetbird.Response
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
class LoggingTailInterceptor(val filter: TextFilter, val charsetIfAbsent: Charset = Charsets.UTF_8) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private companion object {
        const val TAG = "KNetBird"
        val LINE = buildLine(80, '-')
        val HALF_LINE = buildLine(38, '-')

        private fun buildLine(count: Int, c: Char): String = CharArray(count) { c }.let { String(it) }
    }
}
