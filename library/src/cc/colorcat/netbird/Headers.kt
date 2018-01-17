package cc.colorcat.netbird

import cc.colorcat.netbird.internal.Pair
import cc.colorcat.netbird.internal.parseCharset

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
open class Headers internal constructor(protected open val pair: Pair) {
    companion object {
        internal const val CONTENT_TYPE = "Content-Type"
        internal const val CONTENT_LENGTH = "Content-Length"
        internal val emptyHeaders = Headers(Pair.emptyPair)
    }

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

    fun toMultimap() = pair.toMultimap()

    operator fun contains(name: String) = pair.contains(name)

    fun toMutableHeaders() = MutableHeaders(pair.toMutablePair())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Headers

        if (pair != other.pair) return false

        return true
    }

    override fun hashCode(): Int {
        return pair.hashCode()
    }

    override fun toString(): String {
        return "Headers(pair=$pair)"
    }
}
