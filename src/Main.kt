import java.util.*

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */

fun <T> List<T>.toImmutableList(): List<T> {
    return Collections.unmodifiableList(ArrayList(this))
}

fun main(args: Array<String>) {
    val test = mutableMapOf("a" to 1, "b" to 2)
}