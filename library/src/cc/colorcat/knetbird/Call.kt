package cc.colorcat.knetbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Call : Cloneable {
    val request: Request

    @Throws(IOException::class)
    fun execute(): Response

    /**
     * @param callback 异步请求结果回调
     */
    fun enqueue(callback: Callback)

    /**
     * 取消请求
     */
    fun cancel()

    fun isCanceled(): Boolean

    override fun clone(): Call

    interface Factory {
        fun newCall(request: Request): Call
    }
}
