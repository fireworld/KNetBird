package cc.colorcat.netbird

import cc.colorcat.netbird.internal.close
import cc.colorcat.netbird.internal.justDump
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class FileParser private constructor(private val savePath: File) : Parser<File> {
    companion object {
        fun create(savePath: File): FileParser {
            val parent = savePath.parentFile
            if (parent.exists() || parent.mkdirs()) {
                return FileParser(savePath)
            }
            throw RuntimeException("Can't create directory, ${parent.absolutePath}")
        }

        fun create(savePath: String) = create(File(savePath))
    }

    override fun parse(response: Response): NetworkData<File> {
        var output: OutputStream? = null
        var body: ResponseBody? = null
        try {
            output = FileOutputStream(savePath)
            body = response.responseBody as ResponseBody
            body.stream().justDump(output)
            return NetworkData.newSuccess(savePath)
        } finally {
            close(output)
            close(body)
        }
    }
}
