package cc.colorcat.knetbird.logging

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
interface ContentFilter {

    fun filter(contentType: String): Boolean {
        return contentType.contains("utf8", true)
                || contentType.contains("text", true)
                || contentType.contains("json", true)
    }
}
