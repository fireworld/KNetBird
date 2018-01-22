package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.Log
import cc.colorcat.knetbird.internal.checkedHttpUrl
import cc.colorcat.knetbird.internal.toImmutableList
import cc.colorcat.knetbird.platform.Platform
import java.io.File
import java.io.FileNotFoundException
import java.net.Proxy
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class KNetBird(builder: Builder) : Call.Factory {
    val platform = builder.platform
    val baseUrl = builder.baseUrl
    val headInterceptors = builder.headInterceptors.toImmutableList()
    val tailInterceptors = builder.tailInterceptors.toImmutableList()
    val executor: ExecutorService = builder.executor as ExecutorService
    val dispatcher = builder.dispatcher
    val connection = builder.connection
    val proxy = builder.proxy
    val sslSocketFactory = builder.sslSocketFactory
    val hostnameVerifier = builder.hostnameVerifier
    val cacheSize = builder.cacheSize
    val cachePath = builder.cachePath
    val maxRunning = builder.maxRunning
    val readTimeOut = builder.readTimeOut
    val connectTimeOut = builder.connectTimeOut
    val exceptionLogEnabled = builder.exceptionLogEnabled
    val gzipEnabled = builder.gzipEnabled

    init {
        dispatcher.setMaxRunning(maxRunning)
        dispatcher.setExecutor(executor)
        Platform.platform = this.platform
        Log.threshold = builder.logLevel
    }

    override fun newCall(request: Request): Call = RealCall(this, request)

    fun <T> send(request: MRequest<T>): Any {
        newCall(request).enqueue(MCallback(request.parser, request.listener))
        return request.tag
    }

    fun <T> execute(request: MRequest<T>): T? {
        val response = newCall(request).execute()
        if (response.code == 200 && response.responseBody != null) {
            request.parser.parse(response).data
        }
        return null
    }

    fun cancelWaiting(tag: Any) = dispatcher.cancelWaiting(tag)

    fun cancelAll(tag: Any) = dispatcher.cancelAll(tag)

    fun cancelAll() = dispatcher.cancelAll()

    fun newBuilder(): Builder = Builder(this)

    class Builder {
        internal var platform: Platform
        internal var baseUrl: String
        internal var headInterceptors: MutableList<Interceptor>
        internal var tailInterceptors: MutableList<Interceptor>
        internal var executor: ExecutorService?
        internal var dispatcher: Dispatcher
        internal var connection: Connection
        internal var proxy: Proxy?
        internal var sslSocketFactory: SSLSocketFactory?
        internal var hostnameVerifier: HostnameVerifier?
        internal var cacheSize: Long
        internal var cachePath: File?
        internal var maxRunning: Int
        internal var readTimeOut: Int
        internal var connectTimeOut: Int
        internal var exceptionLogEnabled: Boolean
        internal var gzipEnabled: Boolean
        internal var logLevel: Level

        constructor(baseUrl: String) {
            this.platform = Platform.findPlatform()
            this.baseUrl = checkedHttpUrl(baseUrl)
            this.headInterceptors = ArrayList(2)
            this.tailInterceptors = ArrayList(2)
            this.executor = null
            this.dispatcher = Dispatcher()
            this.connection = this.platform.connection
            this.proxy = null
            this.sslSocketFactory = null
            this.hostnameVerifier = null
            this.cacheSize = -1L
            this.cachePath = null
            this.maxRunning = 6
            this.readTimeOut = 10000
            this.connectTimeOut = 10000
            this.exceptionLogEnabled = true
            this.gzipEnabled = false
            this.logLevel = Level.NOTHING
        }

        internal constructor(netBird: KNetBird) {
            this.platform = netBird.platform
            this.baseUrl = netBird.baseUrl
            this.headInterceptors = netBird.headInterceptors.toMutableList()
            this.tailInterceptors = netBird.tailInterceptors.toMutableList()
            this.executor = netBird.executor
            this.dispatcher = netBird.dispatcher
            this.connection = netBird.connection
            this.proxy = netBird.proxy
            this.sslSocketFactory = netBird.sslSocketFactory
            this.hostnameVerifier = netBird.hostnameVerifier
            this.cacheSize = netBird.cacheSize
            this.cachePath = netBird.cachePath
            this.maxRunning = netBird.maxRunning
            this.readTimeOut = netBird.readTimeOut
            this.connectTimeOut = netBird.connectTimeOut
            this.exceptionLogEnabled = netBird.exceptionLogEnabled
            this.gzipEnabled = netBird.gzipEnabled
            this.logLevel = Log.threshold
        }

        fun platform(platform: Platform): Builder {
            this.platform = platform
            this.connection = platform.connection
            return this
        }

        fun addHeadInterceptor(interceptor: Interceptor): Builder {
            headInterceptors.add(interceptor)
            return this
        }

        fun removeHeadInterceptor(interceptor: Interceptor): Builder {
            headInterceptors.remove(interceptor)
            return this
        }

        fun addTailInterceptor(interceptor: Interceptor): Builder {
            tailInterceptors.add(interceptor)
            return this
        }

        fun removeTailInterceptor(interceptor: Interceptor): Builder {
            tailInterceptors.remove(interceptor)
            return this
        }

        fun executor(executor: ExecutorService): Builder {
            this.executor = executor
            return this
        }

        fun connection(connection: Connection): Builder {
            this.connection = connection
            return this
        }

        fun proxy(proxy: Proxy): Builder {
            this.proxy = proxy
            return this
        }

        fun sslSocketFactory(sslSocketFactory: SSLSocketFactory): Builder {
            this.sslSocketFactory = sslSocketFactory
            return this
        }

        fun hostnameVerifier(hostnameVerifier: HostnameVerifier): Builder {
            this.hostnameVerifier = hostnameVerifier
            return this
        }

        fun cache(cachePath: File, cacheSize: Long): Builder {
            if (!cachePath.exists()) {
                throw FileNotFoundException("${cachePath.absolutePath} is not exists")
            }
            if (cacheSize <= 0L) {
                throw IllegalArgumentException("cacheSize($cacheSize) <= 0L")
            }
            this.cachePath = cachePath
            this.cacheSize = cacheSize
            return this
        }

        fun maxRunning(maxRunning: Int): Builder {
            if (maxRunning < 1) {
                throw IllegalArgumentException("maxRunning($maxRunning) < 1")
            }
            this.maxRunning = maxRunning
            return this
        }

        fun readTimeOut(milliseconds: Int): Builder {
            if (milliseconds <= 0) {
                throw IllegalArgumentException("readTimeOut($milliseconds) <= 0")
            }
            this.readTimeOut = milliseconds
            return this
        }

        fun connectTimeOut(milliseconds: Int): Builder {
            if (milliseconds <= 0) {
                throw IllegalArgumentException("connectTimeOut($milliseconds) <= 0")
            }
            this.connectTimeOut = milliseconds
            return this
        }

        fun enableExceptionLog(enabled: Boolean): Builder {
            this.exceptionLogEnabled = enabled
            return this
        }

        fun enableGzip(enabled: Boolean): Builder {
            this.gzipEnabled = enabled
            return this
        }

        fun build(): KNetBird {
            if (executor == null) executor = defaultService(maxRunning)
            return KNetBird(this)
        }

        companion object {
            private fun defaultService(corePoolSize: Int): ExecutorService {
                val executor = ThreadPoolExecutor(
                        corePoolSize,
                        10,
                        60L,
                        TimeUnit.SECONDS,
                        LinkedBlockingDeque<Runnable>(),
                        ThreadPoolExecutor.DiscardOldestPolicy()
                )
                executor.allowCoreThreadTimeOut(true)
                return executor
            }
        }
    }
}
