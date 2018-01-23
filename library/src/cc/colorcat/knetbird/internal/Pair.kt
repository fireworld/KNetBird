package cc.colorcat.knetbird.internal

import java.util.*
import kotlin.collections.HashMap

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */
internal open class Pair internal constructor(
        protected open val names: List<String>,
        protected open val values: List<String>,
        protected val comparator: Comparator<String>
) : PairReader {

    companion object {
        internal val emptyPair = Pair(emptyList(), emptyList(), String.CASE_INSENSITIVE_ORDER)
    }

    final override val size: Int
        get() = names.size

    final override val isEmpty: Boolean
        get() = names.isEmpty()

    override fun names() = names

    override fun values() = values

    final override fun name(index: Int) = names[index]

    final override fun value(index: Int) = values[index]

    final override fun value(name: String): String? {
        return names.indices.firstOrNull { equal(name, names[it]) }?.let { values[it] }
    }

    final override fun value(name: String, defaultValue: String) = value(name) ?: defaultValue

    final override fun values(name: String): List<String> {
        return names.indices.filter { equal(name, names[it]) }.map { values[it] }
    }

    final override fun nameSet(): Set<String> = names.toSortedSet(comparator)

    final override fun toMultimap(): Map<String, List<String>> {
        if (names.isEmpty()) return emptyMap()
        val result = HashMap<String, List<String>>()
        for (name in names) {
            result[name] = values(name)
        }
        return Collections.unmodifiableMap(result)
    }

    final override fun contains(name: String) = names.any { equal(name, it) }

    fun toMutablePair() = MutablePair(names.toMutableList(), values.toMutableList(), comparator)

    protected fun equal(str1: String, str2: String) = comparator.compare(str1, str2) == 0

    override fun iterator(): Iterator<NameAndValue> = PairIterator()

    inner class PairIterator : Iterator<NameAndValue> {
        private val namesItr = names.iterator()
        private val valuesItr = values.iterator()

        override fun hasNext(): Boolean {
            val result = namesItr.hasNext() && valuesItr.hasNext()
            check()
            return result
        }

        override fun next(): NameAndValue {
            val result = NameAndValue(namesItr.next(), valuesItr.next())
            check()
            return result
        }

        private fun check() {
            if (names.size != values.size) {
                throw ConcurrentModificationException()
            }
        }
    }

    internal fun string(separator: String): String {
        val builder = StringBuilder()
        for (index in names.indices) {
            if (index > 0) builder.append(separator)
            builder.append(names[index]).append('=').append(values[index])
        }
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pair

        if (names != other.names) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = names.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }

    final override fun toString(): String {
        return "${javaClass.simpleName}(${string(", ")})"
    }
}
