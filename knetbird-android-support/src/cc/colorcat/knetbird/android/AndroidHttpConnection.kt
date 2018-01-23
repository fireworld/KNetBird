package cc.colorcat.knetbird.android

import android.net.http.HttpResponseCache
import cc.colorcat.knetbird.Connection
import cc.colorcat.knetbird.platform.HttpConnection
import java.io.File

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class AndroidHttpConnection : HttpConnection() {

    override fun clone(): Connection = AndroidHttpConnection()

    override fun enableCache(cachePath: File?, cacheSize: Long) {
        if (cacheSize > 0 && cachePath != null) {
            if (!cacheEnabled) {
                try {
                    var cache: HttpResponseCache? = HttpResponseCache.getInstalled()
                    if (cache == null) {
                        if (cachePath.exists() || cachePath.mkdirs()) {
                            cache = HttpResponseCache.install(cachePath, cacheSize)
                        }
                    }
                    cacheEnabled = (cache != null)
                } catch (e: Exception) {
                    cacheEnabled = false
                }
            }
        } else if (cacheEnabled) {
            cacheEnabled = false
            try {
                val cache = HttpResponseCache.getInstalled()
                cache?.close()
            } catch (ignore: Exception) {
            }
        }
    }
}
