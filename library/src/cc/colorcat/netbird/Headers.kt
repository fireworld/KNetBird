import cc.colorcat.netbird.Pair
import cc.colorcat.netbird.parseCharset
import java.util.*

//package cc.colorcat.netbird
//
///**
// * Created by cxx on 2018/1/16.
// * xx.ch@outlook.com
// */
open class Headers internal constructor(protected open val pair: Pair) {

    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_LENGTH = "Content-Length"
        internal val emptyHeaders = Headers(Pair.emptyPair)

        internal fun create(names: List<String>, values: List<String>): Headers {
            if (names.size != values.size) {
                throw IllegalArgumentException("names.size != values.size")
            }
            return Headers(names, values)
        }

        internal fun create(namesAndValues: Map<String?, List<String?>?>): Headers {
            if (namesAndValues.isEmpty()) return emptyHeaders
            val names = ArrayList<String>(namesAndValues.size)
            val values = ArrayList<String>(namesAndValues.size)
            for ((k, v) in namesAndValues) {
                if (k == null || v == null) continue
                for (value in v) {
                    if (value == null) continue
                    names.add(k)
                    values.add(value)
                }
            }
            return Headers(names, values)
        }
    }

    private constructor(names: List<String>, values: List<String>) : this(Pair.of(names, values, String.CASE_INSENSITIVE_ORDER))

    val names: List<String>
        get() = pair.names()

    val values: List<String>
        get() = pair.values()

    val size
        get() = pair.size

    val isEmpty
        get() = pair.isEmpty

    fun name(index: Int) = pair.name(index)

    fun value(index: Int) = pair.value(index)

    fun value(name: String) = pair.value(name)

    fun value(name: String, defaultValue: String) = pair.value(name, defaultValue)

    fun values(name: String) = pair.values(name)

    fun contentType() = pair.value(CONTENT_TYPE)

    fun contentLength() = pair.value(CONTENT_LENGTH)?.toLong() ?: -1L

    fun charset() = parseCharset(contentType())

    fun nameSet() = pair.nameSet()

    fun toMultimap(): Map<String, List<String>> {
        if (pair.isEmpty) return emptyMap()
        val result = HashMap<String, List<String>>()
        for (name in pair.nameSet()) {
            result[name] = pair.values(name)
        }
        return Collections.unmodifiableMap(result)
    }

    operator fun contains(name: String) = pair.contains(name)

}
