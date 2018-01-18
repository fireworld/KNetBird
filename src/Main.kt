import cc.colorcat.netbird.internal.headersOf
import java.util.*

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */

fun <T> List<T>.toImmutableList(): List<T> {
    return Collections.unmodifiableList(ArrayList(this))
}

fun main(args: Array<String>) {
//    for (i in 0 until 1) {
//        println("hello i = $i")
//    }
//    val headers = mutableHeadersOf(3)
//    headers.add("Content-Type", "text/html")
//    headers.add("Content-Length", "9")
//    println("content-type" in headers)
//    println(headers.value("content-length"))
//    headers.clear()
//    println(headers)

    val names = mutableListOf("a", "b", "c")
    val values = mutableListOf("1", "2", "3")
    val headers = headersOf(names, values)
    println(headers)
}