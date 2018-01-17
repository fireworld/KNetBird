package cc.colorcat.netbird

import cc.colorcat.netbird.internal.ByteOutputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Created by cxx on 18-1-17.
 * xx.ch@outlook.com
 */
class FormBody private constructor(val parameters: Parameters) : RequestBody() {

    companion object {
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
//        const val CONTENT_TYPE = "text/plain; charset=UTF-8"
    }

    private var contentLength = -1L

    override fun contentType() = FormBody.CONTENT_TYPE

    override fun contentLength(): Long {
        return super.contentLength()
    }

    override fun writeTo(os: OutputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun writeOrCountBytes(os: OutputStream, countBytes: Boolean): Long {
        var byteCount = 0L

        val o = if (countBytes) ByteArrayOutputStream() else os
        val bos = ByteOutputStream(o)

        for (i in 0 until parameters.size) {
            if (i > 0) bos.writeByte('&')
            bos.writeUtf8(parameters.name(i))
            bos.writeByte('=')
            bos.writeUtf8(parameters.value(i))
        }
        bos.flush()

        if (countBytes) {
            byteCount = bos.size()
            bos.close()
        }

        return byteCount
    }
}