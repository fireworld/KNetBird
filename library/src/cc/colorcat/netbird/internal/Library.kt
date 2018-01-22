package cc.colorcat.netbird.internal

import cc.colorcat.netbird.*
import cc.colorcat.netbird.platform.Platform
import java.io.*
import java.util.*

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
internal object EmptyListener : MRequest.Listener<Any?> {
    override fun onStart() {}

    override fun onSuccess(result: Any?) {}

    override fun onFailure(code: Int, msg: String) {}

    override fun onFinish() {}
}


private val headersComparator = String.CASE_INSENSITIVE_ORDER
private val parametersComparator = Comparator<String> { str1, str2 -> str1.compareTo(str2) }

fun headersOf(names: List<String>, values: List<String>): Headers {
    return checked(names, values, Headers.emptyHeaders) ?:
            Headers(Pair(names.toImmutableList(), values.toImmutableList(), headersComparator))
}

fun headersOf(namesAndValues: Map<String?, List<String?>?>): Headers {
    if (namesAndValues.isEmpty()) return Headers.emptyHeaders
    val (names, values) = unzip(namesAndValues)
    return headersOf(names, values)
}


fun mutableHeadersOf(names: List<String>, values: List<String>): MutableHeaders {
    return checked(names, values, null) ?:
            MutableHeaders(MutablePair(names.toMutableList(), values.toMutableList(), headersComparator))
}

fun mutableHeadersOf(initCapacity: Int) = MutableHeaders(MutablePair(initCapacity, headersComparator))


fun parametersOf(names: List<String>, values: List<String>): Parameters {
    return checked(names, values, Parameters.emptyParameters) ?:
            Parameters(Pair(names.toImmutableList(), values.toImmutableList(), parametersComparator))
}

fun mutableParametersOf(names: List<String>, values: List<String>): MutableParameters {
    return checked(names, values, null) ?:
            MutableParameters(MutablePair(names.toMutableList(), values.toMutableList(), parametersComparator))
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

fun isTargetThread() = Platform.get().scheduler().isTargetThread()

fun onTargetThread(runnable: Runnable) = Platform.get().scheduler().onTargetThread(runnable)