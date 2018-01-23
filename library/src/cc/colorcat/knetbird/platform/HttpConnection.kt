package cc.colorcat.knetbird.platform

import cc.colorcat.knetbird.*
import cc.colorcat.knetbird.internal.headersOf
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
open class HttpConnection : Connection {
    private lateinit var conn: HttpURLConnection
    private var input: InputStream? = null

    @Throws(IOException::class)
    override fun connect(netBird: KNetBird, request: Request) {
        enableCache(netBird.cachePath, netBird.cacheSize)
        val url = URL(request.url)
        val proxy = netBird.proxy
        conn = (if (proxy == null) url.openConnection() else url.openConnection(proxy)) as HttpURLConnection
        conn.connectTimeout = netBird.connectTimeOut
        conn.readTimeout = netBird.readTimeOut
        conn.doInput = true
        conn.requestMethod = request.method.name
        conn.doOutput = request.method.needBody()
        conn.useCaches = cacheEnabled
        if (conn is HttpsURLConnection) {
            val connection = conn as HttpsURLConnection
            netBird.sslSocketFactory?.apply { connection.sslSocketFactory = this }
            netBird.hostnameVerifier?.apply { connection.hostnameVerifier = this }
        }
    }

    override fun writeHeaders(headers: Headers) {
        for ((name, value) in headers) {
            conn.addRequestProperty(name, value)
        }
    }

    @Throws(IOException::class)
    override fun writeRequestBody(requestBody: RequestBody) {
        val contentLength = requestBody.contentLength()
        if (contentLength > 0L) {
            var output: OutputStream? = null
            try {
                output = conn.outputStream
                requestBody.writeTo(output)
                output.flush()
            } finally {
                cc.colorcat.knetbird.internal.close(output)
            }
        }
    }

    @Throws(IOException::class)
    override fun responseCode(): Int = conn.responseCode

    @Throws(IOException::class)
    override fun responseMsg(): String = conn.responseMessage ?: ""

    @Throws(IOException::class)
    override fun responseHeaders(): Headers {
        val headersMultimap = conn.headerFields ?: return Headers.emptyHeaders
        return headersOf(headersMultimap)
    }

    @Throws(IOException::class)
    override fun responseBody(headers: Headers): ResponseBody? {
        if (input == null) {
            input = conn.inputStream
        }
        return if (input != null) {
            ResponseBody.create(input as InputStream, headers)
        } else {
            null
        }
    }

    override fun cancel() {
        if (this::conn.isInitialized) {
            conn.disconnect()
        }
    }

    override fun clone(): Connection = HttpConnection()

    override fun close() {
        cc.colorcat.knetbird.internal.close(input)
        if (this::conn.isInitialized) {
            conn.disconnect()
        }
    }

    private companion object {
        private var CacheEnabled = false
    }

    protected var cacheEnabled: Boolean
        get() = HttpConnection.CacheEnabled
        set(value) {
            HttpConnection.CacheEnabled = value
        }

    open protected fun enableCache(cachePath: File?, cacheSize: Long) {
        cacheEnabled = cachePath != null && cacheSize > 0
    }
}
