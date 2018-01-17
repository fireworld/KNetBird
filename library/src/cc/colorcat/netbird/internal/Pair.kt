package cc.colorcat.netbird.internal

import java.util.*

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */
internal open class Pair internal constructor(
        protected open val names: List<String>,
        protected open val values: List<String>,
        protected val comparator: Comparator<String>
) {
    companion object {
        internal val emptyPair = Pair(emptyList(), emptyList(), String.CASE_INSENSITIVE_ORDER)
    }

    val size
        get() = names.size

    val isEmpty
        get() = names.isEmpty()


    open fun names() = names

    open fun values() = values

    fun name(index: Int) = names[index]

    fun value(index: Int) = values[index]

    fun value(name: String): String? {
        return names.indices.firstOrNull { equal(name, names[it]) }?.let { values[it] }
    }

    fun value(name: String, defaultValue: String) = value(name) ?: defaultValue

    fun values(name: String): List<String> {
        return names.indices.filter { equal(name, names[it]) }.map { values[it] }
    }

    fun nameSet(): Set<String> = names.toSortedSet(comparator)

    fun toMultimap(): Map<String, List<String>> {
        if (names.isEmpty()) return emptyMap()
        val result = HashMap<String, List<String>>()
        for (name in names) {
            result[name] = values(name)
        }
        return Collections.unmodifiableMap(result)
    }

    fun toMutablePair() = MutablePair(names.toMutableList(), values.toMutableList(), comparator)

    operator fun contains(name: String) = names.any { equal(name, it) }

    protected fun equal(str1: String, str2: String) = comparator.compare(str1, str2) == 0

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

    override fun toString(): String {
        return "Pair(names=$names, values=$values)"
    }
}
