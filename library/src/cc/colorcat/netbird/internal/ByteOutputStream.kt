package cc.colorcat.netbird.internal

import java.io.FilterOutputStream
import java.io.OutputStream


/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
internal class ByteOutputStream(output: OutputStream) : FilterOutputStream(output) {
    private var written = 0L

    @Synchronized
    override fun write(b: Int) {
        out.write(b)
        incCount(1)
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }

    @Synchronized
    override fun write(b: ByteArray, off: Int, len: Int) {
        out.write(b, off, len)
        incCount(len)
    }

    fun writeUtf8(s: String) {
        val bytes = s.toByteArray(Charsets.UTF_8)
        this.write(bytes, 0, bytes.size)
    }

    fun writeByte(c: Char) {
        this.write(c.toInt())
    }

    fun size() = written

    private fun incCount(value: Int) {
        var temp = written + value
        if (temp < 0L) {
            temp = Long.MAX_VALUE
        }
        written = temp
    }
}