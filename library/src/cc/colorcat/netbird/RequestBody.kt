package cc.colorcat.netbird

import java.io.OutputStream

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
abstract class RequestBody {

    abstract fun contentType(): String

    open fun contentLength() = -1L

    abstract fun writeTo(output: OutputStream)
}