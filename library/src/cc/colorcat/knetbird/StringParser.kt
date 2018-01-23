package cc.colorcat.knetbird

import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class StringParser private constructor(private val charset: Charset) : Parser<String> {
    companion object {
        val utf8 by lazy { StringParser(Charsets.UTF_8) }

        fun create(charset: Charset) = if (charset == Charsets.UTF_8) utf8 else StringParser(charset)

        fun create(charset: String) = create(Charset.forName(charset))
    }

    @Throws(IOException::class)
    override fun parse(response: Response): NetworkData<String> {
        val body = response.responseBody as ResponseBody
        return NetworkData.newSuccess(body.string(charset))
    }
}
