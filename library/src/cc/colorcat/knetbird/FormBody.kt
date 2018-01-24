package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.ByteOutputStream
import cc.colorcat.knetbird.internal.mutableParametersOf
import cc.colorcat.knetbird.internal.smartEncode
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Created by cxx on 18-1-17.
 * xx.ch@outlook.com
 */
internal class FormBody private constructor(internal val parameters: Parameters) : RequestBody() {
    internal companion object {
        const val CONTENT_TYPE = "application/x-www-form-urlencoded"

        internal fun create(namesAndValues: Parameters, needEncode: Boolean = true): FormBody {
            if (!needEncode) {
                return FormBody(namesAndValues)
            }
            val encoded = mutableParametersOf(namesAndValues.size)
            for (i in 0 until namesAndValues.size) {
                val name = namesAndValues.name(i)
                val value = namesAndValues.value(i)
                encoded.add(smartEncode(name), smartEncode(value))
            }
            return FormBody(encoded.toParameters())
        }
    }

    private var contentLength = -1L

    override fun contentType() = FormBody.CONTENT_TYPE

    @Throws(IOException::class)
    override fun contentLength(): Long {
        if (contentLength == -1L) {
            val length = writeOrCountBytes(null, true)
            if (length > 0L) {
                contentLength = length
            }
        }
        return contentLength
    }

    @Throws(IOException::class)
    override fun writeTo(output: OutputStream) {
        writeOrCountBytes(output, false)
    }

    @Throws(IOException::class)
    private fun writeOrCountBytes(output: OutputStream?, countBytes: Boolean): Long {
        var byteCount = 0L

        val os = if (countBytes) ByteArrayOutputStream() else output as OutputStream
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
