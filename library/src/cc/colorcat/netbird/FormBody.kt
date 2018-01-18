package cc.colorcat.netbird

import cc.colorcat.netbird.internal.ByteOutputStream
import cc.colorcat.netbird.internal.emptyOutputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Created by cxx on 18-1-17.
 * xx.ch@outlook.com
 */
internal class FormBody private constructor(internal val parameters: Parameters) : RequestBody() {

    companion object {
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"
//        const val CONTENT_TYPE = "text/plain; charset=UTF-8"
    }

    private var contentLength = -1L

    override fun contentType() = FormBody.CONTENT_TYPE

    override fun contentLength(): Long {
        if (contentLength == -1L) {
            val length = writeOrCountBytes(emptyOutputStream, true)
            if (length > 0L) {
                contentLength = length
            }
        }
        return contentLength
    }

    override fun writeTo(output: OutputStream) {
        writeOrCountBytes(output, false)
    }

    private fun writeOrCountBytes(output: OutputStream, countBytes: Boolean): Long {
        var byteCount = 0L

        val os = if (countBytes) ByteArrayOutputStream() else output
        val bos = ByteOutputStream(os)

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FormBody

        if (parameters != other.parameters) return false

        return true
    }

    override fun hashCode(): Int {
        return parameters.hashCode()
    }

    override fun toString(): String {
        return "FormBody(parameters=$parameters)"
    }
}
