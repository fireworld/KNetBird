package cc.colorcat.knetbird.internal

import cc.colorcat.knetbird.*
import cc.colorcat.knetbird.platform.Platform
import java.io.*
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
private val httpRegex = "^(http)(s)?://(\\S)+".toRegex()

internal fun checkedHttpUrl(url: String): String {
    if (!url.toLowerCase().matches(httpRegex)) {
        throw IllegalArgumentException("Bad url = $url, the scheme must be http or https")
    }
    return url
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


internal val emptyListener: MRequest.Listener<Any?> by lazy {
    object : MRequest.Listener<Any?> {
        override fun onStart() {}

        override fun onSuccess(result: Any?) {}

        override fun onFailure(code: Int, msg: String) {}

        override fun onFinish() {}
    }
}

/**
 * A Comparator that orders strings ignoring character case.
 */
private val headersComparator = String.CASE_INSENSITIVE_ORDER

fun headersOf(names: List<String>, values: List<String>): Headers {
    checkSize(names, values)
    return if (names.isEmpty()) {
        Headers.emptyHeaders
    } else {
        Headers(Pair(names.toImmutableList(), values.toImmutableList(), headersComparator))
    }
}

fun headersOf(namesAndValues: Map<String?, List<String?>?>): Headers {
    if (namesAndValues.isEmpty()) return Headers.emptyHeaders
    val (names, values) = unzip(namesAndValues)
    return headersOf(names, values)
}

fun mutableHeadersOf(names: List<String>, values: List<String>): MutableHeaders {
    checkSize(names, values)
    return MutableHeaders(MutablePair(names.toMutableList(), values.toMutableList(), headersComparator))
}

fun mutableHeadersOf(initCapacity: Int) = MutableHeaders(MutablePair(initCapacity, headersComparator))

internal fun checkHeader(name: String, value: String) {
    if (name.isEmpty()) throw  IllegalArgumentException("name is empty")
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


private val parametersComparator = Comparator<String> { str1, str2 -> str1.compareTo(str2) }

fun parametersOf(names: List<String>, values: List<String>): Parameters {
    checkSize(names, values)
    return if (names.isEmpty()) {
        Parameters.emptyParameters
    } else {
        Parameters(Pair(names.toImmutableList(), values.toImmutableList(), parametersComparator))
    }
}

fun parametersOf(namesAndValues: Map<String?, List<String?>?>): Parameters {
    if (namesAndValues.isEmpty()) return Parameters.emptyParameters
    val (names, values) = unzip(namesAndValues)
    return parametersOf(names, values)
}

fun mutableParametersOf(names: List<String>, values: List<String>): MutableParameters {
    checkSize(names, values)
    return MutableParameters(MutablePair(names.toMutableList(), values.toMutableList(), parametersComparator))
}

fun mutableParametersOf(initCapacity: Int) = MutableParameters(MutablePair(initCapacity, parametersComparator))


private fun unzip(namesAndValues: Map<String?, List<String?>?>): kotlin.Pair<List<String>, List<String>> {
    val size = namesAndValues.size
    val names = ArrayList<String>(size)
    val values = ArrayList<String>(size)
    for ((name, valueList) in namesAndValues) {
        if (name == null || valueList == null) continue
        for (value in valueList) {
            if (value == null) continue
            names.add(name)
            values.add(value)
        }
    }
    return kotlin.Pair(names, values)
}

private fun checkSize(names: List<String>, values: List<String>) {
    if (names.size != values.size) {
        throw IllegalArgumentException("names.size(${names.size}) != values.size(${values.size}")
    }
}


internal fun <T> List<T>.toImmutableList(): List<T> = Collections.unmodifiableList(ArrayList(this))

@Throws(IOException::class)
internal fun InputStream.justDump(output: OutputStream) {
    val bis = this as? BufferedInputStream ?: BufferedInputStream(this)
    val bos = output as? BufferedOutputStream ?: BufferedOutputStream(output)
    val buffer = ByteArray(2048)
    var length = bis.read(buffer)
    while (length != -1) {
        bos.write(buffer, 0, length)
        length = bis.read(buffer)
    }
    bos.flush()
}

fun isTargetThread() = Platform.get().scheduler.isTargetThread()

fun onTargetThread(runnable: Runnable) = Platform.get().scheduler.onTargetThread(runnable)
