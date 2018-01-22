package cc.colorcat.knetbird.internal

import cc.colorcat.knetbird.Level
import cc.colorcat.knetbird.platform.Platform

/**
 * Created by cxx on 2018/1/22.
 * xx.ch@outlook.com
 */
internal class Log private constructor() {
    internal companion object {
        internal var threshold = Level.NOTHING

        fun v(tag: String, msg: String) = log(tag, msg, Level.VERBOSE)

        fun d(tag: String, msg: String) = log(tag, msg, Level.VERBOSE)

        fun i(tag: String, msg: String) = log(tag, msg, Level.VERBOSE)

        fun w(tag: String, msg: String) = log(tag, msg, Level.VERBOSE)

        fun e(tag: String, msg: String) = log(tag, msg, Level.VERBOSE)

        fun e(e: Throwable) {
            if (Level.ERROR.priority >= threshold.priority) {
                e.printStackTrace()
            }
        }

        private fun log(tag: String, msg: String, level: Level) {
            if (level.priority >= threshold.priority) {
                Platform.get().logger.log(tag, msg, level)
            }
        }
    }
}
