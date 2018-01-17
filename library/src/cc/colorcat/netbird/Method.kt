package cc.colorcat.netbird

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
enum class Method {
    GET, HEAD, TRACE, OPTIONS, POST, PUT, DELETE;

    fun needBody() = when (this) {
        POST, PUT, DELETE -> true
        else -> false
    }
}