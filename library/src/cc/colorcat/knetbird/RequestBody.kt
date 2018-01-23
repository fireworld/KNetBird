package cc.colorcat.knetbird

import java.io.IOException
import java.io.OutputStream

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
abstract class RequestBody {

    abstract fun contentType(): String

    @Throws(IOException::class)
    open fun contentLength() = -1L

    @Throws(IOException::class)
    abstract fun writeTo(output: OutputStream)
}
