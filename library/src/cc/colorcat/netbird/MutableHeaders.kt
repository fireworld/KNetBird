package cc.colorcat.netbird

import cc.colorcat.netbird.internal.MutablePair

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
class MutableHeaders internal constructor(override val pair: MutablePair) : Headers(pair) {

    fun add(name: String, value: String) = pair.add(name, value)

    fun addAll(names: List<String>, value: List<String>) = pair.addAll(names, values)

    fun set(name: String, value: String) = pair.set(name, value)

    fun addIfNot(name: String, value: String) = pair.addIfNot(name, value)

    fun removeAll(name: String) = pair.removeAll(name)

    fun clear() = pair.clear()

    fun toHeaders() = Headers(pair.toPair())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutableHeaders

        if (pair != other.pair) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + pair.hashCode()
        return result
    }

    override fun toString(): String {
        return "MutableHeaders(pair=$pair)"
    }
}
