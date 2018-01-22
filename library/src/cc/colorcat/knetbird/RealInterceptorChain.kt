package cc.colorcat.knetbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
internal class RealInterceptorChain internal constructor(
        private val interceptors: List<Interceptor>,
        private val index: Int,
        override val request: Request,
        override val connection: Connection
) : Interceptor.Chain {

    @Throws(IOException::class)
    override fun proceed(request: Request): Response {
        val next = RealInterceptorChain(interceptors, index + 1, request, connection)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}