package cc.colorcat.knetbird

import java.io.IOException
import java.util.zip.GZIPInputStream

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class GzipInterceptor(private val gzipEnabled: Boolean) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!gzipEnabled) return chain.proceed(chain.request)
        var transparentGzip = false
        val builder = chain.request.unfreeze().newBuilder()
        if (builder.headerValue("Accept-Encoding") == null && builder.headerValue("Range") == null) {
            transparentGzip = true
            builder.addHeader("Accept-Encoding", "gzip")
        }
        var response = chain.proceed(builder.build().freeze())
        if (transparentGzip && "gzip".equals(response.header("Content-Encoding"), true)) {
            val original = response.responseBody
            if (original != null) {
                val newStream = GZIPInputStream(original.stream())
                val newBuilder = response.newBuilder()
                newBuilder.removeHeader("Content-Encoding")
                        .removeHeader("Content-Length")
                val newBody = ResponseBody.create(newStream, newBuilder.headers)
                response = newBuilder.responseBody(newBody).build()
            }
        }
        return response
    }
}
