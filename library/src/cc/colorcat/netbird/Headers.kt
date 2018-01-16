//package cc.colorcat.netbird
//
///**
// * Created by cxx on 2018/1/16.
// * xx.ch@outlook.com
// */
//open class Headers protected constructor(names: MutableList<String>, values: MutableList<String>) {
//    companion object {
//        const val CONTENT_TYPE = "Content-Type"
//        const val CONTENT_LENGTH = "Content-Length"
//
//        val emptyHeaders = Headers(ArrayList(0), ArrayList(0))
//
//        internal fun create(names: MutableList<String>, values: MutableList<String>): Headers {
//            if (names.size != values.size) {
//                throw IllegalArgumentException("names.size != values.size")
//            }
//            return Headers(names, values)
//        }
//
//        internal fun create(namesAndValues: Map<String?, List<String?>?>): Headers {
//            if (namesAndValues.isEmpty()) return emptyHeaders
//            val names = ArrayList<String>(namesAndValues.size)
//            val values = ArrayList<String>(namesAndValues.size)
//            for ((k, v) in namesAndValues) {
//                if (k == null || v == null) continue
//                for (value in v) {
//                    if (value == null) continue
//                    names.add(k)
//                    values.add(value)
//                }
//            }
//            return Headers(names, values)
//        }
//    }
//
//    protected val pair = Pair.of(names, values, String.CASE_INSENSITIVE_ORDER)
//
//    val names: List<String>
//        get() = pair.names()
//
//    val values: List<String>
//        get() = pair.values()
//
//    val size
//        get() = pair.size
//
//    val isEmpty
//        get() = pair.isEmpty
//
//    fun name(index: Int) = pair.name(index)
//
//    fun value(index: Int) = pair.value(index)
//
//    fun value(name: String) = pair.value(name)
//
//    fun value(name: String, defaultValue: String) = pair.value(name, defaultValue)
//
//    fun values(name: String) = pair.values(name)
//
//    fun contentType() = pair.value(CONTENT_TYPE)
//
//    fun contentLength() = pair.value(CONTENT_LENGTH)?.toLong() ?: -1L
//
//    fun charset() = parseCharset(pair.value(CONTENT_TYPE))
//
//    fun nameSet() = pair.nameSet()
//
//    fun toMultimap() = pair.toMultimap()
//
//    fun contains(name: String) = pair.contains(name)
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as Headers
//
//        if (pair != other.pair) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        return pair.hashCode()
//    }
//
//    override fun toString(): String {
//        return "{$javaClass}(pair=$pair)"
//    }
//}
