package cc.colorcat.netbird

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
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

    override fun contentType(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contentLength(): Long {
        return super.contentLength()
    }

    override fun writeTo(os: OutputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    private fun writeOrCountBytes(os: OutputStream?, countBytes: Boolean): Long {
//        var byteCount = 0L
//
//        val o = if (countBytes) ByteArrayOutputStream() else os
//        val bos = ByteOutputStream(o)
//
//        var i = 0
//        val size = parameters.size()
//        while (i < size) {
//            if (i > 0) bos.writeByte('&')
//            bos.writeUtf8(namesAndValues.name(i))
//            bos.writeByte('=')
//            bos.writeUtf8(namesAndValues.value(i))
//            i++
//        }
//        bos.flush()
//
//        if (countBytes) {
//            byteCount = bos.size()
//            bos.close()
//        }
//
//        return byteCount
//    }
}