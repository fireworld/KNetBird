package cc.colorcat.knetbird.internal

import java.io.FilterOutputStream
import java.io.IOException
import java.io.OutputStream


/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
internal class ByteOutputStream(output: OutputStream) : FilterOutputStream(output) {
    private var written = 0L

    private fun incCount(value: Int) {
        var temp = written + value
        if (temp < 0L) {
            temp = Long.MAX_VALUE
        }
        written = temp
    }

    @Synchronized
    @Throws(IOException::class)
    override fun write(b: Int) {
        out.write(b)
        incCount(1)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        this.write(b, 0, b.size)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        out.write(b, off, len)
        incCount(len)
    }

    @Throws(IOException::class)
    fun writeUtf8(s: String) {
        val bytes = s.toByteArray(Charsets.UTF_8)
        this.write(bytes, 0, bytes.size)
    }

    @Throws(IOException::class)
    fun writeByte(c: Char) {
        this.write(c.toInt())
    }

    @Synchronized
    fun size() = written
}
