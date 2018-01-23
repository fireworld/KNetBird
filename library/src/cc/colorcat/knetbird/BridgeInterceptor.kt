package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.ProgressInputStream
import cc.colorcat.knetbird.internal.Version
import cc.colorcat.knetbird.internal.smartEncode
import java.io.IOException
import java.net.URI

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class BridgeInterceptor(private val baseUrl: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request.newBuilder()
        var uri = URI.create(builder.url.takeUnless { it.isEmpty() } ?: baseUrl)
        if (!builder.path.isEmpty()) uri = uri.resolve(builder.path)
        var url = uri.toString()

        if (!builder.method.needBody()) {
            concatParameters(builder.names(), builder.values())?.also {
                url = url + '?' + it
                builder.clear()
            }
        } else {
            builder.requestBody?.also {
                builder.setHeader(Headers.CONTENT_TYPE, it.contentType())
                val contentLength = it.contentLength()
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

        var response = chain.proceed(builder.build().freeze())
        builder.downloadListener?.also {
            response.responseBody?.apply {
                val contentLength = this.contentLength()
                if (contentLength > 0L) {
                    val newStream = ProgressInputStream.wrap(this.stream(), contentLength, it)
                    val newBody = ResponseBody.create(newStream, this.contentType(), contentLength, this.charset())
                    response = response.newBuilder().responseBody(newBody).build()
                }
            }
        }
        return response
    }

    private companion object {
        private fun concatParameters(names: List<String>, values: List<String>): String? {
            if (names.isEmpty()) return null
            val sb = StringBuilder()
            for (i in names.indices) {
                if (i > 0) sb.append('&')
                val encodedName = smartEncode(names[i])
                val encodedValue = smartEncode(values[i])
                sb.append(encodedName).append('=').append(encodedValue)
            }
            return sb.toString()
        }
    }
}
