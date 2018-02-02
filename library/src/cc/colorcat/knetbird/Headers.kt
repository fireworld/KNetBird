package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.Pair
import cc.colorcat.knetbird.internal.PairReader
import cc.colorcat.knetbird.internal.parseCharset

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
open class Headers internal constructor(protected open val pair: Pair) : PairReader by pair {

    companion object {
        internal const val CONTENT_TYPE = "Content-Type"
        internal const val CONTENT_LENGTH = "Content-Length"
        internal val emptyHeaders = Headers(Pair.emptyPair)
    }

    fun contentType() = pair.value(Headers.CONTENT_TYPE)

    fun contentLength() = pair.value(Headers.CONTENT_LENGTH)?.toLong() ?: -1L

    fun charset() = parseCharset(pair.value(Headers.CONTENT_TYPE))

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

    final override fun toString(): String {
        return "${javaClass.simpleName}(${pair.string(", ")})"
    }
}
