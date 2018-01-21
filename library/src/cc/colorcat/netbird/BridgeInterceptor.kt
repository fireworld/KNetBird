package cc.colorcat.netbird

import cc.colorcat.netbird.internal.Version
import cc.colorcat.netbird.internal.smartEncode
import java.io.IOException
import java.net.URI

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class BridgeInterceptor(private val baseUrl: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request
        val builder = request.newBuilder()
        var uri = URI.create(builder.url.takeUnless { it.isEmpty() } ?: baseUrl)
        val path = builder.path
        if (!path.isEmpty()) uri = uri.resolve(path)
        var url = uri.toString()

        if (!builder.method.needBody()) {
            val parameters = concatParameters(builder.names(), builder.values())
            if (parameters != null) {
                url = url + '?' + parameters
                builder.clear()
            }
        } else {
            val body = request.requestBody
            if (body != null) {
                builder.setHeader(Headers.CONTENT_TYPE, body.contentType())
                val contentLength = body.contentLength()
                if (contentLength > 0L) {
                    builder.setHeader(Headers.CONTENT_LENGTH, contentLength.toString())
                            .removeHeader("Transfer-Encoding")
                } else {
                    builder.setHeader("Transfer-Encoding", "chunked")
                            .removeHeader(Headers.CONTENT_LENGTH)
                }
            }
        }
        builder.url(url)
                .path("")
                .addHeaderIfNot("Connection", "Keep-Alive")
                .addHeaderIfNot("User-Agent", Version.userAgent())
        return chain.proceed(builder.build().freeze())
    }

    companion object {
        private fun concatParameters(names: List<String>, values: List<String>): String? {
            if (names.isEmpty()) return null
            val sb = StringBuilder()
            for (i in 0 until names.size) {
                if (i > 0) sb.append('&')
                val encodedName = smartEncode(names[i])
                val encodedValue = smartEncode(values[i])
                sb.append(encodedName).append('=').append(encodedValue)
            }
            return sb.toString()
        }
    }
}