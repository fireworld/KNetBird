package cc.colorcat.netbird.internal

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
internal interface PairReader : Iterable<NameAndValue> {
    val size: Int

    val isEmpty: Boolean

    fun names(): List<String>

    fun values(): List<String>

    fun name(index: Int): String

    fun value(index: Int): String

    fun value(name: String): String?

    fun value(name: String, defaultValue: String): String

    fun values(name: String): List<String>

    fun nameSet(): Set<String>

    fun toMultimap(): Map<String, List<String>>

    operator fun contains(name: String): Boolean
}