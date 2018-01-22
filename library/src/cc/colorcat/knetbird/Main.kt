package cc.colorcat.knetbird

import java.net.URI

/**
 * Created by cxx on 2018/1/15.
 * xx.ch@outlook.com
 */

fun main(args: Array<String>) {
    val names = mutableListOf("this", "id", "test", "haha", "ID", "cxx")
    val values = mutableListOf("a", "b", "c", "d", "e", "you")
    val uri = URI("http://www.baidu.com/path/")
    val path = "test/haha"
    println(uri.resolve(path))
}