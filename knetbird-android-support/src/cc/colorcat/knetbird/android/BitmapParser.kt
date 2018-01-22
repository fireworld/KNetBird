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
        val bitmap = if (reqWidth > 0 && reqHeight > 0) {
            val bytes = body.bytes()
            decodeStream(ByteArrayInputStream(bytes), reqWidth, reqHeight)
        } else {
            BitmapFactory.decodeStream(body.stream())
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
            var result: Bitmap? = null
            val bis = BufferedInputStream(input)
            bis.mark(bis.available())
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(bis, null, options)
            if (options.outWidth != -1 && options.outHeight != -1) {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
                options.inJustDecodeBounds = false
                bis.reset()
                result = BitmapFactory.decodeStream(bis, null, options)
            }
            return result
        }

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height shr 1
                val halfWidth = width shr 1
                while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                    inSampleSize = inSampleSize shl 1
                }
            }
            return inSampleSize
        }
    }
}