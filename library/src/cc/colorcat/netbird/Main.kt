package cc.colorcat.netbird

import cc.colorcat.netbird.internal.headersOf

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */

fun main(args: Array<String>) {
    val names = mutableListOf("this", "id", "test", "haha", "ID", "cxx")
    val values = mutableListOf("a", "b", "c", "d", "e", "you")
//    val pair = Pair.of(names, values)
//    val contentType = "Content-Type: text/plain; charset=utf-8"
//    val contentType = "Content-Type: text/plain; charset=gbk"
//    val test = parseCharset(contentType)
//    println(test)
//    val headers = Headers(pair)
    val headers = headersOf(emptyList(), emptyList())
    println(headers === Headers.emptyHeaders)
}