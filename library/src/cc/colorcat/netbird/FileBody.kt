package cc.colorcat.netbird

import cc.colorcat.netbird.internal.ProgressInputStream
import cc.colorcat.netbird.internal.close
import cc.colorcat.netbird.internal.justDump
import java.io.File
import java.io.FileNotFoundException
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
                throw FileNotFoundException("${file.absolutePath} is not exists")
            }
            return FileBody(name, contentType, file, listener)
        }
    }

    override fun contentType() = contentType

    @Throws(IOException::class)
    override fun contentLength() = file.length()

    @Throws(IOException::class)
    override fun writeTo(output: OutputStream) {
        val input = ProgressInputStream.wrap(file, listener)
        try {
            input.justDump(output)
        } finally {
            close(input)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileBody

        if (name != other.name) return false
        if (contentType != other.contentType) return false
        if (file != other.file) return false
        if (listener != other.listener) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + file.hashCode()
        result = 31 * result + (listener?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "FileBody(name='$name', contentType='$contentType', file=$file, listener=$listener)"
    }
}
