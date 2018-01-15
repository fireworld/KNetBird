package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */
internal class Pair(private val names: MutableList<String>,
                    private val values: MutableList<String>,
                    private val comparator: Comparator<String>) {

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
        this.names.addAll(names)
        this.values.addAll(values)
    }

    fun set(name: String, value: String) {
        removeAll(name)
        add(name, value)
    }

    fun addIfNot(name: String, value: String) {
        if (!contains(name)) {
            add(name, value)
        }
    }

    fun removeAll(name: String) {
        names.indices.filter { equal(name, names[it]) }
                .forEach {
                    names.removeAt(it)
                    values.removeAt(it)
                }
    }

    fun contains(name: String): Boolean {
        return names.any { equal(name, it) }
    }

    private fun equal(str1: String, str2: String): Boolean {
        return comparator.compare(str1, str2) == 0
    }
}