package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.StateIOException
import java.io.IOException

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Callback {
    fun onStart()

    @Throws(IOException::class)
    fun onResponse(call: Call, response: Response)

    fun onFailure(call: Call, cause: StateIOException)

    fun onFinish()
}
