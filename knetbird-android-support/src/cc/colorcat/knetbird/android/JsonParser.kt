package cc.colorcat.knetbird.android

import cc.colorcat.knetbird.NetworkData
import cc.colorcat.knetbird.Parser
import cc.colorcat.knetbird.Response
import cc.colorcat.knetbird.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class JsonParser private constructor(private val charset: Charset = Charsets.UTF_8) : Parser<JSONObject> {
    companion object {
        val utf8 by lazy { JsonParser() }

        fun create(charset: Charset) = if (charset == Charsets.UTF_8) utf8 else JsonParser(charset)

        fun create(charset: String) = create(Charset.forName(charset))
    }

    override fun parse(response: Response): NetworkData<JSONObject> {
        val body = response.responseBody as ResponseBody
        try {
            val jsonObj = JSONObject(body.string(charset))
            return NetworkData.newSuccess(jsonObj)
        } catch (e: JSONException) {
            throw IOException(e)
        }
    }
}
