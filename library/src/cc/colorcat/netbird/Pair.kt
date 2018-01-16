package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */
internal open class Pair protected constructor(
        protected open val names: List<String>,
        protected open val values: List<String>,
        private val comparator: Comparator<String>
) {

    companion object {
        internal fun of(names: List<String>, values: List<String>, comparator: Comparator<String> = Comparator { o1, o2 -> o1.compareTo(o2) }): Pair {
            if (names.size != values.size) {
                throw IllegalArgumentException("names.size != values.size")
            }
            return Pair(names.toImmutableList(), values.toImmutableList(), comparator)
        }
    }

    val size
        get() = names.size

    val isEmpty
        get() = names.isEmpty()


    open fun names(): List<String> {
        return names
    }

    open fun values(): List<String> {
        return values
    }

    fun name(index: Int): String {
        return names[index]
    }

    fun value(index: Int): String {
        return values[index]
    }

    fun value(name: String): String? {
        return names.indices.firstOrNull { equal(name, names[it]) }?.let { values[it] }
    }

    fun value(name: String, defaultValue: String): String {
        return value(name) ?: defaultValue
    }

    fun values(name: String): List<String> {
        return names.indices.filter { equal(name, names[it]) }.map { values[it] }
    }

    fun nameSet(): Set<String> {
        return names.toSortedSet(comparator)
    }

    protected fun equal(str1: String, str2: String): Boolean {
        return comparator.compare(str1, str2) == 0
    }
}