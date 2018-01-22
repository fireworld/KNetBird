package cc.colorcat.knetbird

import java.io.Closeable
import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Connection : Closeable, Cloneable {
    @Throws(IOException::class)
    fun connect(netBird: KNetBird, request: Request)

    @Throws(IOException::class)
    fun writeHeaders(headers: Headers)

    @Throws(IOException::class)
    fun writeRequestBody(requestBody: RequestBody)

    @Throws(IOException::class)
    fun responseCode(): Int

    @Throws(IOException::class)
    fun responseMsg(): String

    @Throws(IOException::class)
    fun responseHeaders(): Headers

    @Throws(IOException::class)
    fun responseBody(headers: Headers): ResponseBody?

    fun cancel()

    public override fun clone(): Connection
}