package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.mutableParametersOf

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */

fun main(args: Array<String>) {
    val names = mutableListOf("this", "id", "test", "haha", "ID", "cxx")
    val values = mutableListOf("a", "b", "c", "d", "e", "you")
//    val pair = Pair(names, values, String.CASE_INSENSITIVE_ORDER)
//    print(pair)
//    val headers = mutableHeadersOf(names, values)
//    println(headers)
    val params = mutableParametersOf(names, values)
    println(params)
}