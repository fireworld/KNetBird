package cc.colorcat.netbird

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */
internal class Pair private constructor(private val names: MutableList<String>,
                                        private val values: MutableList<String>,
                                        private val comparator: Comparator<String>) {
    companion object {
        fun ofWithIgnoreCase(names: MutableList<String>, values: MutableList<String>)
                = of(names, values, String.CASE_INSENSITIVE_ORDER)

        fun of(names: MutableList<String>, values: MutableList<String>, comparator: Comparator<String> = kotlin.Comparator { o1, o2 -> o1.compareTo(o2) }): Pair {
            if (names.size != values.size) {
                throw IllegalArgumentException("names.size != values.size")
            }
            return Pair(names, values, comparator)
        }

        fun create(initCapacity: Int, comparator: Comparator<String> = kotlin.Comparator { o1, o2 -> o1.compareTo(o2) }): Pair {
            return Pair(ArrayList(initCapacity), ArrayList(initCapacity), comparator)
        }
    }

    val size
        get() = names.size

    val isEmpty
        get() = names.isEmpty()

    fun names(): List<String> {
        return names.toList()
    }

    fun values(): List<String> {
        return values.toList()
    }

    fun name(index: Int): String {
        return names[index]
    }

    fun value(index: Int): String {
        return values[index]
    }

    fun value(name: String): String? {
        return names.indices.firstOrNull { equal(name, names[it]) }?.let { return values[it] }
    }

    fun value(name: String, defaultValue: String): String {
        return value(name) ?: defaultValue
    }

    fun values(name: String): List<String> {
        return names.indices.filter { equal(name, names[it]) }.map { values[it] }
    }

    fun add(name: String, value: String) {
        names.add(name)
        values.add(value)
    }

    fun addAll(names: List<String>, values: List<String>) {
        if (names.size != values.size) {
            throw IllegalArgumentException("names.size != values.size")
        }
        this.names.addAll(names)
        this.values.addAll(values)
    }

    fun set(name: String, value: String) {
        remove(name)
        add(name, value)
    }

    fun addIfNot(name: String, value: String) {
        if (!contains(name)) {
            add(name, value)
        }
    }

    fun remove(name: String) {
        for (index in names.lastIndex downTo 0) {
            if (equal(name, names[index])) {
                names.removeAt(index)
                values.removeAt(index)
            }
        }
    }

    fun contains(name: String): Boolean {
        return names.any { equal(name, it) }
    }

    fun clear() {
        names.clear()
        values.clear()
    }

    fun nameSet(): Set<String> {
        return names.toSortedSet(comparator)
    }

    fun toMultimap(): Map<String, List<String>> {
        if (names.isEmpty()) return emptyMap()
        val result = mutableMapOf<String, List<String>>()
        for (name in nameSet()) {
            result[name] = values(name)
        }
        return result.toMap()
    }

    private fun equal(str1: String, str2: String): Boolean {
        return comparator.compare(str1, str2) == 0
    }
}