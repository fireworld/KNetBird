package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Interceptor {
    @Throws(IOException::class)
    fun intercept(chain: Chain): Response

    interface Chain {
        val connection: Connection

        val request: Request

        @Throws(IOException::class)
        fun proceed(request: Request): Response
    }
}