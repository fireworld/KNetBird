package cc.colorcat.netbird.internal

import cc.colorcat.netbird.ProgressListener
import java.io.Closeable
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 2018/1/16.
 * xx.ch@outlook.com
 */

fun postProgressOnTargetThread(finished: Long, total: Long, percent: Int, listener: ProgressListener) {

}

fun parseCharset(contentType: String?): Charset? {
    return contentType
            ?.split(";")
            ?.map { it.trim().split("=") }
            ?.filter { it.size == 2 && "charset".equals(it[0], true) }
            ?.let { if (it.isEmpty()) null else Charset.forName(it[0][1]) }
}

fun close(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (ignore: IOException) {

        }
    }
}
