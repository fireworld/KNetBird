package cc.colorcat.netbird

import cc.colorcat.netbird.internal.ProgressInputStream
import cc.colorcat.netbird.internal.close
import cc.colorcat.netbird.internal.justDump
import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
class FileBody private constructor(
        internal val name: String,
        private val contentType: String,
        internal val file: File,
        private val listener: UploadListener?
) : RequestBody() {
    companion object {
        internal fun create(name: String, contentType: String, file: File, listener: UploadListener?): FileBody {
            if (!file.exists()) {
                throw IOException("${file.absolutePath} is not exists")
            }
            return FileBody(name, contentType, file, listener)
        }
    }

    override fun contentType() = contentType

    override fun contentLength() = file.length()

    override fun writeTo(os: OutputStream) {
        val input = ProgressInputStream.wrap(file, listener)
        try {
            input.justDump(os)
        } finally {
            close(input)
        }
    }
}