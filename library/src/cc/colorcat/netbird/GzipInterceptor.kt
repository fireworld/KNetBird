package cc.colorcat.netbird

import java.io.IOException
import java.util.zip.GZIPInputStream

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class GzipInterceptor(private val gzipEnabled: Boolean) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!gzipEnabled) return chain.proceed(chain.request())
        var transparentGzip = false
        val builder = chain.request().unfreeze().newBuilder()
        if (builder.headerValue("Accept-Encoding") == null && builder.headerValue("Range") == null) {
            transparentGzip = true
            builder.addHeader("Accept-Encoding", "gzip")
        }
        var response = chain.proceed(builder.build().freeze())
        if (transparentGzip && "gzip".equals(response.header("Content-Encoding"), true)) {
            val original = response.responseBody
            if (original != null) {
                val input = GZIPInputStream(original.stream())
                val newBody = ResponseBody.create(input,
                        original.contentType(),
                        original.contentLength(),
                        original.charset())
                response = response.newBuilder()
                        .responseBody(newBody)
                        .removeHeader("Content-Encoding")
                        .removeHeader("Content-Length")
                        .build()
            }
        }
        return response
    }
}
