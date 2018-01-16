package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
internal class MutablePair private constructor(
        override val names: MutableList<String>,
        override val values: MutableList<String>,
        comparator: Comparator<String>
) : Pair(names, values, comparator) {

    override fun names(): List<String> {
        return names.toImmutableList()
    }

    override fun values(): List<String> {
        return values.toImmutableList()
    }

    fun add(name: String, value: String) {
        names.add(name)
        values.add(value)
    }
}