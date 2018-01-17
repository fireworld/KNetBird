package cc.colorcat.netbird.internal

import cc.colorcat.netbird.ProgressListener
import java.io.File
import java.io.FileInputStream
import java.io.FilterInputStream
import java.io.InputStream

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
class ProgressInputStream private constructor(
        input: InputStream,
        private val contentLength: Long,
        private val listener: ProgressListener
) : FilterInputStream(input) {
    companion object {
        internal fun wrap(file: File, listener: ProgressListener?) = wrap(FileInputStream(file), file.length(), listener)

        internal fun wrap(input: InputStream, contentLength: Long, listener: ProgressListener?) =
                if (listener != null && contentLength > 0L) {
                    ProgressInputStream(input, contentLength, listener)
                } else {
                    input
                }
    }

    override fun read(): Int {
        val nextByte = `in`.read()
        if (nextByte != -1) {
            updateProgress(1)
        }
        return nextByte
    }

    override fun read(b: ByteArray): Int {
        return this.read(b, 0, b.size)
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val count = `in`.read(b, off, len)
        if (count != -1) {
            updateProgress(count)
        }
        return count
    }

    private var finished = 0L
    private var currentPercent = 0
    private var lastPercent = currentPercent

    private fun updateProgress(readCount: Int) {
        finished += readCount
        currentPercent = (finished * 100 / contentLength).toInt()
        if (currentPercent > lastPercent) {
            postProgressOnTargetThread(finished, contentLength, currentPercent, listener)
            lastPercent = currentPercent
        }
    }
}
