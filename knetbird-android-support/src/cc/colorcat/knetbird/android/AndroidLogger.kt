package cc.colorcat.knetbird.android

import android.util.Log
import cc.colorcat.knetbird.Level
import cc.colorcat.knetbird.platform.Logger

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class AndroidLogger : Logger {
    override fun log(tag: String, msg: String, level: Level) {
        val priority = when (level) {
            Level.VERBOSE -> Log.VERBOSE
            Level.DEBUG -> Log.DEBUG
            Level.INFO -> Log.INFO
            Level.WARN -> Log.WARN
            Level.ERROR -> Log.ERROR
            Level.NOTHING -> Int.MAX_VALUE
        }
        if (priority != Int.MAX_VALUE) {
            AndroidLogger.println(priority, tag, msg)
        }
    }

    companion object {
        private const val MAX_LENGTH = 1024 * 2

        private fun println(priority: Int, tag: String, msg: String) {
            var start = 0
            var end = start + MAX_LENGTH
            val size = msg.length
            while (start < size) {
                if (end >= size) {
                    Log.println(priority, tag, msg.substring(start))
                } else {
                    Log.println(priority, tag, msg.substring(start, end))
                }
                start = end
                end = start + MAX_LENGTH
            }
        }
    }
}