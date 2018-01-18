package cc.colorcat.netbird.internal

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
internal interface PairWriter {

    fun add(name: String, value: String)

    fun addIfNot(name: String, value: String)

    fun addAll(names: List<String>, values: List<String>)

    fun set(name: String, value: String)

    fun removeAll(name: String)

    fun clear()
}