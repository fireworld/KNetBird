package cc.colorcat.netbird

import java.nio.charset.Charset

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */
fun parseCharset(contentType: String?): Charset? {
    return contentType
            ?.split(";")
            ?.map { it.trim().split("=") }
            ?.filter { it.size == 2 && it[0].equals("charset", true) }
            ?.let { Charset.forName(it[0][1]) }
}