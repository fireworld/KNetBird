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
        internal fun of(names: List<String>, values: List<String>, comparator: Comparator<String> = String.CASE_SENSITIVE_ORDER): Pair {
            if (names.size != values.size) {
                throw IllegalArgumentException("names.size != values.size")
            }
            return Pair(names.toImmutableList(), values.toImmutableList(), comparator)
        }

        internal val emptyPair = Pair(emptyList(), emptyList(), String.CASE_SENSITIVE_ORDER)
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

    operator fun contains(name: String): Boolean {
        return names.any { equal(name, it) }
    }

    protected fun equal(str1: String, str2: String): Boolean {
        return comparator.compare(str1, str2) == 0
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

    override fun toString(): String {
        return "Pair(names=$names, values=$values)"
    }
}