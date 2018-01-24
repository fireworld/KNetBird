package cc.colorcat.knetbird.logging

/**
 * Created by cxx on 18-1-23.
 * xx.ch@outlook.com
 */
interface ContentFilter {

    fun filter(contentType: String): Boolean = TextReg.matches(contentType.toLowerCase())

    private companion object {
        val TextReg = ".*(charset|text|html|htm|json|urlencoded)+.*".toRegex()
    }
}
