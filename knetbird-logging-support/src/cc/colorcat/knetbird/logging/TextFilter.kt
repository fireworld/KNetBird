package cc.colorcat.knetbird.logging

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
interface TextFilter {
    fun filter(contentType: String): Boolean
}