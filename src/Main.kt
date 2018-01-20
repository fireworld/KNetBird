import java.net.URI
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

//    val names = mutableListOf("this", "is", "test")
//    val values = mutableListOf("1", "2", "3")
//    val headers = mutableHeadersOf(names, values)
//    println(headers)
//    for ((name, value) in headers) {
//        println("name = $name, value = $value")
//    }
//    println("-------------")
//    headers.filter { it.name.length > 3 }.forEach { println(it) }
//    val itr = headers.iterator()
//    while (itr.hasNext()) {
//        if (itr.next().name.length > 3) {
//            itr.remove()
//        }
//    }
//    for ((name, value) in headers) {
//        println("name = $name, value = $value")
//    }
//    val uri = URI.create("http://www.baidu.com/test/")
//    val new = uri.resolve("/path")
//    println(new)
    val a = 23
    var b: Int
    val scanner = Scanner(System.`in`)
    print("Input a number: ")
    b = scanner.nextInt()
    val c by lazy {
        println("c executed")
        a + b
    }
    println("a = $a, b = $b")
    println("c = $c")
    print("Input another number: ")
    b += 10
    println("a = $a, b = $b")
    println("c = $c")
}
