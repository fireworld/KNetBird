package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.close
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class StringParser private constructor(private val charset: Charset = Charsets.UTF_8) : Parser<String> {
    companion object {
        val utf8 by lazy { StringParser() }

        fun create(charset: Charset) = if (charset == Charsets.UTF_8) utf8 else StringParser(charset)

        fun create(charset: String) = StringParser(Charset.forName(charset))
    }

    override fun parse(response: Response): NetworkData<String> {
        val body = response.responseBody as ResponseBody
        try {
            return NetworkData.newSuccess(body.string(charset))
        } finally {
            close(body)
        }
    }
}
