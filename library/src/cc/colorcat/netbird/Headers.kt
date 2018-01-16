package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
internal open class Headers private constructor(private val pair: Pair) {
    companion object {
        const val CONTENT_TYPE = "Content-Type"
        const val CONTENT_LENGTH = "Content-Length"
    }

    internal constructor(names: MutableList<String>, values: MutableList<String>) : this(Pair.of(names, values, String.CASE_INSENSITIVE_ORDER))

    val names
        get() = pair.names()

    val values
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

    fun charset() = parseCharset(pair.value(CONTENT_TYPE))

    fun nameSet() = pair.nameSet()

    fun toMultimap() = pair.toMultimap()

    fun contains(name: String) = pair.contains(name)
}
