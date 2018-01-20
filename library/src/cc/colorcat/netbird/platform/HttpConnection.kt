package cc.colorcat.netbird.platform

import cc.colorcat.netbird.*
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
open class HttpConnection : Connection {
    protected var cacheEnabled = false
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
        conn as HttpURLConnection
        val newConn = conn as HttpURLConnection
        newConn.connectTimeout = netBird.connectTimeOut
        newConn.readTimeout = netBird.readTimeOut
        newConn.doInput = true
        val method = request.method
        newConn.requestMethod = method.name
        newConn.doOutput = method.needBody()
        newConn.useCaches = cacheEnabled
        if (newConn is HttpsURLConnection) {
            val connection = newConn as HttpsURLConnection
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
    }

    override fun writeRequestBody(requestBody: RequestBody) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseCode(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseMsg(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseHeaders(): Headers {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun responseBody(headers: Headers): ResponseBody? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone(): Connection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    open protected fun enableCache(path: File?, cacheSize: Long) {
        cacheEnabled = path != null && cacheSize > 0
    }
}