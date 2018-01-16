package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */

fun main(args: Array<String>) {
    val names = mutableListOf("this", "id", "test", "haha", "ID", "cxx")
    val values = mutableListOf("a", "b", "c", "d", "e", "you")
    val pair = Pair.of(names, values, String.CASE_INSENSITIVE_ORDER)
    println(pair.value("this"))
    println(pair.value("hahas", "on null test"))
    println(pair.values("id"))
    println(pair.toMultimap())
    println(pair.values("word"))
}