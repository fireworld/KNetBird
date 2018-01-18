package cc.colorcat.netbird.internal

import cc.colorcat.netbird.Headers
import cc.colorcat.netbird.MutableHeaders
import cc.colorcat.netbird.MutableParameters
import cc.colorcat.netbird.Parameters
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
internal val emptyOutputStream = object : OutputStream() {
    override fun write(b: Int) {}
}

private val headersComparator = String.CASE_INSENSITIVE_ORDER
private val parametersComparator = Comparator<String> { str1, str2 -> str1.compareTo(str2) }

fun headersOf(names: List<String>, values: List<String>): Headers {
    return checked(names, values, Headers.emptyHeaders) ?: Headers(Pair(names, values, headersComparator))
}

fun headersOf(namesAndValues: Map<String?, List<String?>?>): Headers {
    if (namesAndValues.isEmpty()) return Headers.emptyHeaders
    val (names, values) = unzip(namesAndValues)
    return headersOf(names, values)
}


fun mutableHeadersOf(names: MutableList<String>, values: MutableList<String>): MutableHeaders {
    return checked(names, values, null) ?: MutableHeaders(MutablePair(names, values, headersComparator))
}

fun mutableHeadersOf(initCapacity: Int) = MutableHeaders(MutablePair(initCapacity, headersComparator))


fun parametersOf(names: List<String>, values: List<String>): Parameters {
    return checked(names, values, Parameters.emptyParameters) ?: Parameters(Pair(names, values, parametersComparator))
}

fun mutableParametersOf(names: MutableList<String>, values: MutableList<String>): MutableParameters {
    return checked(names, values, null) ?: MutableParameters(MutablePair(names, values, parametersComparator))
}

fun mutableParametersOf(initCapacity: Int) = MutableParameters(MutablePair(initCapacity, parametersComparator))

private fun unzip(namesAndValues: Map<String?, List<String?>?>): kotlin.Pair<List<String>, List<String>> {
    val names = ArrayList<String>(namesAndValues.size)
    val values = ArrayList<String>(namesAndValues.size)
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

private fun <R> checked(names: List<String>, values: List<String>, valueOnEmpty: R?): R? {
    if (names.size != values.size) {
        throw IllegalArgumentException("names.size != values.size")
    }
    return if (names.isEmpty()) valueOnEmpty else null
}

internal fun <T> List<T>.toImmutableList(): List<T> = Collections.unmodifiableList(ArrayList(this))

internal fun <K, V> Map<K, V>.toImmutableMap(): Map<K, V> = Collections.unmodifiableMap(HashMap(this))

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
