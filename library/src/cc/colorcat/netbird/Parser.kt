package cc.colorcat.netbird

import java.io.IOException

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
interface Parser<out T> {

    @Throws(IOException::class)
    fun parse(response: Response): NetworkData<T>
}
