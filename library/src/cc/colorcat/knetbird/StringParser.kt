package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.close
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class StringParser private constructor(private val charset: Charset?) : Parser<String> {

    companion object {
        val utf8 by lazy { StringParser(Charsets.UTF_8) }

        val noCharset by lazy { StringParser(null) }

        fun create(charset: Charset) = StringParser(charset)

        fun create(charset: String) = StringParser(Charset.forName(charset))
    }

    override fun parse(response: Response): NetworkData<String> {
        val body = response.responseBody as ResponseBody
        try {
            return if (charset != null)
                NetworkData.newSuccess(body.string(charset))
            else
                NetworkData.newSuccess(body.string())
        } finally {
            close(body)
        }
    }
}