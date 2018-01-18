package cc.colorcat.netbird

import cc.colorcat.netbird.internal.ByteOutputStream
import cc.colorcat.netbird.internal.emptyOutputStream
import java.io.ByteArrayOutputStream
import java.io.OutputStream

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
internal class MultipartBody private constructor(
        private val formBody: FormBody,
        private val fileBodies: List<FileBody>,
        private val boundary: String) : RequestBody() {

    companion object {
        private const val MIX = "multipart/form-data; boundary="
        private val CRLF = byteArrayOf('\r'.toByte(), '\n'.toByte())
        private val DASH_DASH = byteArrayOf('-'.toByte(), '-'.toByte())

        fun create(formBody: FormBody, fileBodies: List<FileBody>, boundary: String): MultipartBody {
            return MultipartBody(formBody, fileBodies, boundary)
        }
    }

    private var contentLength = -1L

    override fun contentType() = MultipartBody.MIX + boundary

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

        val params = formBody.parameters
        for (i in 0 until params.size) {
            val name = params.name(i)
            val value = params.value(i)

            bos.write(DASH_DASH)
            bos.writeUtf8(boundary)
            bos.write(CRLF)

            bos.writeUtf8("Content-Disposition: form-data; name=\"" + name + "\"")
            bos.write(CRLF)

            bos.writeUtf8("Content-Type: " + formBody.contentType())
            bos.write(CRLF)

            bos.write(CRLF)
            bos.writeUtf8(value)
            bos.write(CRLF)
        }

        for (body in fileBodies) {
            val contentLength = body.contentLength()
            if (contentLength != -1L) {
                bos.write(DASH_DASH)
                bos.writeUtf8(boundary)
                bos.write(CRLF)

                bos.writeUtf8("Content-Disposition: form-data; name=\"" + body.name + "\"; filename=\"" + body.file.name + "\"")
                bos.write(CRLF)

                bos.writeUtf8("Content-Type: " + body.contentType())
                bos.write(CRLF)

                bos.writeUtf8("Content-Transfer-Encoding: BINARY")
                bos.write(CRLF)

                bos.write(CRLF)
                if (countBytes) {
                    byteCount += contentLength
                } else {
                    body.writeTo(bos)
                }

                bos.write(CRLF)
            } else if (countBytes) {
                bos.close()
                return -1L
            }
        }

        bos.write(CRLF)
        bos.write(DASH_DASH)
        bos.writeUtf8(boundary)
        bos.write(DASH_DASH)
        bos.write(CRLF)
        bos.flush()

        if (countBytes) {
            byteCount += bos.size()
            bos.close()
        }

        return byteCount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MultipartBody

        if (formBody != other.formBody) return false
        if (fileBodies != other.fileBodies) return false
        if (boundary != other.boundary) return false

        return true
    }

    override fun hashCode(): Int {
        var result = formBody.hashCode()
        result = 31 * result + fileBodies.hashCode()
        result = 31 * result + boundary.hashCode()
        return result
    }

    override fun toString(): String {
        return "MultipartBody(formBody=$formBody, fileBodies=$fileBodies, boundary='$boundary')"
    }
}
