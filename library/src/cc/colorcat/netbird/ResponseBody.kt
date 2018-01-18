package cc.colorcat.netbird

import cc.colorcat.netbird.internal.parseCharset
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.io.InputStream
import java.io.Reader
import java.nio.charset.Charset

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
abstract class ResponseBody : Closeable {

    abstract fun contentType(): String?

    abstract fun contentLength(): Long

    abstract fun charset(): Charset?

    abstract fun stream(): InputStream

    fun reader(): Reader {
        val charset = charset()
        return if (charset != null) stream().bufferedReader(charset) else stream().bufferedReader()
    }

    fun reader(charsetIfAbsent: Charset): Reader {
        val charset = charset() ?: charsetIfAbsent
        return stream().bufferedReader(charset)
    }

    fun string() = reader().readText()

    fun string(charsetIfAbsent: Charset) = reader(charsetIfAbsent).readText()

    fun bytes() = stream().readBytes()

    final override fun close() {
        cc.colorcat.netbird.internal.close(stream())
    }

    companion object {
        fun create(content: String, contentType: String? = null, charset: Charset? = parseCharset(contentType)): ResponseBody {
            val bytes = if (charset != null) content.toByteArray(charset) else content.toByteArray()
            return create(bytes, contentType, charset)
        }

        fun create(content: ByteArray, contentType: String? = null, charset: Charset? = parseCharset(contentType)): ResponseBody {
            val input = ByteArrayInputStream(content)
            val length = content.size.toLong()
            return create(input, contentType, length, charset)
        }

        fun create(input: InputStream, contentType: String? = null, contentLength: Long = -1L, charset: Charset? = parseCharset(contentType)): ResponseBody {
            return object : ResponseBody() {
                override fun contentLength() = contentLength

                override fun contentType() = contentType

                override fun charset() = charset

                override fun stream() = input
            }
        }
    }
}