package cc.colorcat.netbird.platform

import cc.colorcat.netbird.*
import cc.colorcat.netbird.internal.headersOf
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
    companion object {
        protected var cacheEnabled = false
    }

    private lateinit var conn: HttpURLConnection
    private var input: InputStream? = null
    private var listener: DownloadListener? = null

    @Throws(IOException::class)
    override fun connect(netBird: KNetBird, request: Request) {
        listener = request.downloadListener
        enableCache(netBird.cachePath, netBird.cacheSize)
        val url = URL(request.url)
        val proxy = netBird.proxy
        conn = (if (proxy == null) url.openConnection() else url.openConnection(proxy)) as HttpURLConnection
        conn.connectTimeout = netBird.connectTimeOut
        conn.readTimeout = netBird.readTimeOut
        conn.doInput = true
        val method = request.method
        conn.requestMethod = method.name
        conn.doOutput = method.needBody()
        conn.useCaches = cacheEnabled
        if (conn is HttpsURLConnection) {
            val connection = conn as HttpsURLConnection
            val factory = netBird.sslSocketFactory
            if (factory != null) {
                connection.sslSocketFactory = factory
            }
            val verifier = netBird.hostnameVerifier
            if (verifier != null) {
                connection.hostnameVerifier = verifier
            }
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
                cc.colorcat.netbird.internal.close(output)
            }
        }
    }

    @Throws(IOException::class)
    override fun responseCode(): Int = conn.responseCode

    @Throws(IOException::class)
    override fun responseMsg(): String = conn.responseMessage ?: ""

    @Throws(IOException::class)
    override fun responseHeaders(): Headers {
        val headers = conn.headerFields ?: return Headers.emptyHeaders
        return headersOf(headers)
    }

    @Throws(IOException::class)
    override fun responseBody(headers: Headers): ResponseBody? {
        if (input == null) {
            input = conn.inputStream
        }
        return if (input != null) {
            RealResponseBody.create(input as InputStream, headers, listener)
        } else {
            null
        }
    }

    override fun cancel() {
        if (this::conn.isInitialized) {
            conn.disconnect()
        }
    }

    override fun clone(): Connection {
        return HttpConnection()
    }

    override fun close() {
        cc.colorcat.netbird.internal.close(input)
        if (this::conn.isInitialized) {
            conn.disconnect()
        }
    }

    open protected fun enableCache(cachePath: File?, cacheSize: Long) {
        cacheEnabled = cachePath != null && cacheSize > 0
    }
}