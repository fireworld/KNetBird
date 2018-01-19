package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class RealInterceptorChain internal constructor(
        private val interceptors: List<Interceptor>,
        private val index: Int,
        private val request: Request,
        private val connection: Connection
) : Interceptor.Chain {
    override fun connection(): Connection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun request(): Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Throws(IOException::class)
    override fun proceed(request: Request): Response {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}