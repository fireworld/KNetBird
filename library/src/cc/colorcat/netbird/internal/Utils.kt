package cc.colorcat.netbird.internal

import cc.colorcat.netbird.ProgressListener
import java.io.Closeable
import java.io.IOException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */

internal fun postProgressOnTargetThread(finished: Long, total: Long, percent: Int, listener: ProgressListener) {

}

internal fun checkedHttpUrl(url: String): String {
    if (!url.toLowerCase().matches("^(http)(s)?://(\\S)+".toRegex())) {
        throw IllegalArgumentException("Bad url = $url, the scheme must be http or https")
    }
    return url
}

internal fun checkHeader(name: String, value: String) {
    if (name.isEmpty()) throw  IllegalArgumentException("name is empty");
    for (c in name) {
        if (c <= '\u001f' || c >= '\u007f') {
            throw IllegalArgumentException("Unexpected char $c in header name: $name")
        }
    }
    for (c in value) {
        if (c <= '\u001f' || c >= '\u007f') {
            throw IllegalArgumentException("Unexpected char $c in header value: $value")
        }
    }
}

internal fun parseCharset(contentType: String?): Charset? {
    return contentType
            ?.split(";")
            ?.map { it.trim().split("=") }
            ?.filter { it.size == 2 && "charset".equals(it[0], true) }
            ?.let { if (it.isEmpty()) null else Charset.forName(it[0][1]) }
}

internal fun close(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (ignore: IOException) {

        }
    }
}

internal fun <T : CharSequence> emptyElse(cs: T?, defaultValue: T) = if (cs == null || cs.isEmpty()) defaultValue else cs

internal fun smartEncode(s: String): String {
    try {
        val decoded = decode(s)
        if (s != decoded) {
            return s
        }
    } catch (e: Exception) {

    }
    return encode(s)
}

private fun encode(s: String): String = URLEncoder.encode(s, Charsets.UTF_8.name())

private fun decode(s: String): String = URLDecoder.decode(s, Charsets.UTF_8.name())
