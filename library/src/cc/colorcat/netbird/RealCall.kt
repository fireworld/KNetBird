package cc.colorcat.netbird

import cc.colorcat.netbird.internal.StateIOException
import cc.colorcat.netbird.internal.close
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class RealCall(private val netBird: KNetBird, override val request: Request) : Call {
    private val connection = netBird.connection.clone()
    private val executed = AtomicBoolean(false)
    private val canceled = AtomicBoolean(false)

//    override fun request(): Request = request

    @Throws(IOException::class)
    override fun execute(): Response {
        if (executed.getAndSet(true)) throw IllegalStateException("Already executed")
        if (!netBird.dispatcher.executed(this)) throw StateIOException(HttpStatus.MSG_DUPLICATE_REQUEST, HttpStatus.CODE_DUPLICATE_REQUEST)
        try {
            return getResponseWithInterceptorChain()
        } finally {
            netBird.dispatcher.finished(this)
        }
    }

    override fun enqueue(callback: Callback) {
        if (executed.getAndSet(true)) throw IllegalStateException("Already executed")
        callback.onStart()
        netBird.dispatcher.enqueue(AsyncCall(callback))
    }

    @Throws(IOException::class)
    private fun getResponseWithInterceptorChain(): Response {
        val headInterceptors = netBird.headInterceptors
        val tailInterceptors = netBird.tailInterceptors
        val size = headInterceptors.size + tailInterceptors.size + 3
        val interceptors = ArrayList<Interceptor>(size)
        interceptors.addAll(headInterceptors)
        interceptors.add(BridgeInterceptor(netBird.baseUrl))
        interceptors.addAll(tailInterceptors)
        interceptors.add(GzipInterceptor(netBird.gzipEnabled))
        interceptors.add(ConnectInterceptor(netBird))
        val chain = RealInterceptorChain(interceptors, 0, request, connection)
        return chain.proceed(request)
    }

    override fun cancel() {
        canceled.set(true)
        connection.cancel()
    }

    override fun isCanceled(): Boolean = canceled.get()

    override fun clone(): Call = RealCall(netBird, request)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RealCall

        if (request != other.request) return false
        if (executed != other.executed) return false
        if (canceled != other.canceled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = request.hashCode()
        result = 31 * result + executed.hashCode()
        result = 31 * result + canceled.hashCode()
        return result
    }

    override fun toString(): String {
        return "RealCall(request=$request, executed=$executed, canceled=$canceled)"
    }


    internal inner class AsyncCall(internal val callback: Callback) : Runnable {

        val request: Request = this@RealCall.request

        val call: RealCall = this@RealCall

//        fun get() = this@RealCall

        override fun run() {
            var code = HttpStatus.CODE_CONNECT_ERROR
            var msg: String? = null
            try {
                if (this@RealCall.canceled.get()) {
                    callback.onFailure(
                            this@RealCall,
                            StateIOException(HttpStatus.MSG_REQUEST_CANCELED, HttpStatus.CODE_REQUEST_CANCELED)
                    )
                } else {
                    val response = getResponseWithInterceptorChain()
                    code = response.code
                    msg = response.msg
                    callback.onResponse(this@RealCall, response)
                }
            } catch (e: IOException) {
                e.printStackTrace() // TODO 稍后需要更改
                msg = if (msg == null) {
                    e.message ?: HttpStatus.MSG_CONNECT_ERROR
                } else {
                    "Response msg = $msg\nException detail = " + e.toString()
                }
                callback.onFailure(this@RealCall, StateIOException(msg as String, code, e))
            } finally {
                callback.onFinish()
                netBird.dispatcher.finished(this)
                close(this@RealCall.connection)
            }
        }

//        fun request() = this@RealCall.request

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AsyncCall

            if (callback != other.callback) return false

            return this@RealCall == other.call
        }

        override fun hashCode(): Int {
            var result = callback.hashCode()
            result = 31 * result + this@RealCall.hashCode()
            return result
        }

        override fun toString(): String {
            return "AsyncCall(Call = ${this@RealCall}, callback=$callback)"
        }
    }
}
