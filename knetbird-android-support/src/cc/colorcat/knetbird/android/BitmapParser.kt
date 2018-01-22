package cc.colorcat.knetbird.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cc.colorcat.knetbird.NetworkData
import cc.colorcat.knetbird.Parser
import cc.colorcat.knetbird.Response
import cc.colorcat.knetbird.ResponseBody
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class BitmapParser private constructor(
        private val reqWidth: Int = -1,
        private val reqHeight: Int = -1
) : Parser<Bitmap> {

    override fun parse(response: Response): NetworkData<Bitmap> {
        val body = response.responseBody as ResponseBody
        val bitmap: Bitmap? = body.use {
            if (reqWidth > 0 && reqHeight > 0) {
                val bytes = it.bytes()
                decodeStream(ByteArrayInputStream(bytes), reqWidth, reqHeight)
            } else {
                BitmapFactory.decodeStream(it.stream())
            }
        }
        return if (bitmap != null) {
            NetworkData.newSuccess(bitmap)
        } else {
            NetworkData.newFailure(response.code, response.msg)
        }
    }

    companion object {
        val default by lazy {
            BitmapParser()
        }

        fun create(reqWidth: Int, reqHeight: Int): BitmapParser {
            if (reqWidth <= 0 || reqHeight <= 0) {
                throw IllegalArgumentException("reqWidth and reqHeight must be greater than 0")
            }
            return BitmapParser(reqWidth, reqHeight)
        }

        @Throws(IOException::class)
        private fun decodeStream(input: InputStream, reqWidth: Int, reqHeight: Int): Bitmap? {
            val bis = BufferedInputStream(input)
            bis.mark(bis.available())
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(bis, null, options)
            bis.reset()
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(bis, null, options)
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val width = options.outWidth
            val height = options.outHeight
            var inSampleSize = 1
            while (width / inSampleSize > reqWidth && height / inSampleSize > reqHeight) {
                inSampleSize = inSampleSize shl 1
            }
            return inSampleSize
        }
    }
}