package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Callback {
    fun onStart()

    @Throws(IOException::class)
    fun onResponse(call: Call, response: Response)

    fun onFailure(call: Call, e: IOException)

    fun onFinish()
}