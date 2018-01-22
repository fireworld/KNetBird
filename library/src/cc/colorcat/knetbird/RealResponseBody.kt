package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.ProgressInputStream
import java.io.InputStream

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class RealResponseBody private constructor(
        private val input: InputStream,
        private val headers: Headers
) : ResponseBody() {

    companion object {
        fun create(input: InputStream, headers: Headers) = RealResponseBody(input, headers)

        fun create(input: InputStream, headers: Headers, listener: DownloadListener?): RealResponseBody {
            val pis = ProgressInputStream.wrap(input, headers.contentLength(), listener)
            return RealResponseBody(pis, headers)
        }
    }

    override fun contentType() = headers.contentType()

    override fun contentLength() = headers.contentLength()

    override fun charset() = headers.charset()

    override fun stream() = input
}