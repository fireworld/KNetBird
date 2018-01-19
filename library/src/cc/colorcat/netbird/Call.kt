package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Call : Cloneable {
    fun request(): Request

    @Throws(IOException::class)
    fun execute(): Response

    /**
     * @param callback 异步请求结果回调
     * @throws NullPointerException 如果 callback 为空会抛出此异常
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