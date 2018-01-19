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

    override fun connection() = connection

    override fun request() = request

    @Throws(IOException::class)
    override fun proceed(request: Request): Response {
        val next = RealInterceptorChain(interceptors, index + 1, request, connection)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}