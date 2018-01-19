package cc.colorcat.netbird

import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class RealCall(private val netBird: KNetBird, private val request: Request) : Call {
    private val connection = netBird.connection.clone()
    private val executed = AtomicBoolean(false)
    private val canceled = AtomicBoolean(false)

    override fun request(): Request = request

    @Throws(IOException::class)
    override fun execute(): Response {
        // TODO 仍未完成
        return getResponseWithInterceptorChain()
    }

    override fun enqueue(callback: Callback) {
    }

    @Throws(IOException::class)
    private fun getResponseWithInterceptorChain(): Response {
        // TODO 还未完成
        val chain = RealInterceptorChain(listOf(), 0, request, connection)
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

        override fun run() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun request() = this@RealCall.request

        fun get() = this@RealCall

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as AsyncCall

            if (callback != other.callback) return false

            return this@RealCall == other.get()
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
