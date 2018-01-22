package cc.colorcat.knetbird.internal

import java.util.*

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
internal class MutablePair internal constructor(
        override val names: MutableList<String>,
        override val values: MutableList<String>,
        comparator: Comparator<String>
) : Pair(names, values, comparator), PairWriter {

    internal constructor(
            initCapacity: Int,
            comparator: Comparator<String>
    ) : this(ArrayList(initCapacity), ArrayList(initCapacity), comparator)

    override fun names() = names.toImmutableList()

    override fun values() = values.toImmutableList()

    override fun add(name: String, value: String) {
        names.add(name)
        values.add(value)
    }

    override fun addIfNot(name: String, value: String) {
        if (name !in this) {
            add(name, value)
        }
    }

    override fun addAll(names: List<String>, values: List<String>) {
        if (names.size != values.size) {
            throw IllegalArgumentException("names.size != values.size")
        }
        this.names.addAll(names)
        this.values.addAll(values)
    }

    override fun set(name: String, value: String) {
        removeAll(name)
        add(name, value)
    }

    override fun removeAll(name: String) {
        for (index in names.lastIndex downTo 0) {
            if (equal(name, names[index])) {
                names.removeAt(index)
                values.removeAt(index)
            }
        }
    }

    override fun clear() {
        names.clear()
        values.clear()
    }

    fun toPair() = Pair(names.toImmutableList(), values.toImmutableList(), comparator)

    override fun iterator(): MutableIterator<NameAndValue> = MutablePairIterator()

    inner class MutablePairIterator : MutableIterator<NameAndValue> {
        private val namesItr = names.listIterator()
        private val valuesItr = values.listIterator()

        override fun hasNext(): Boolean {
            val result = namesItr.hasNext()
            check()
            return result
        }

        override fun next(): NameAndValue {
            val result = NameAndValue(namesItr.next(), valuesItr.next())
            check()
            return result
        }

        override fun remove() {
            check()
            namesItr.remove()
            valuesItr.remove()
            check()
        }

        private fun check() {
            if (names.size != values.size) {
                throw ConcurrentModificationException()
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MutablePair

        if (names != other.names) return false
        if (values != other.values) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + names.hashCode()
        result = 31 * result + values.hashCode()
        return result
    }

    override fun toString(): String {
        return "MutablePair(names=$names, values=$values)"
    }
}
