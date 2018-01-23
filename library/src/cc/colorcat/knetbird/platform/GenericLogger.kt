package cc.colorcat.knetbird.platform

import cc.colorcat.knetbird.KNetBird
import cc.colorcat.knetbird.Level
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class GenericLogger : Logger {

    private companion object {
        private val Logger = java.util.logging.Logger.getLogger(KNetBird::class.simpleName)

        init {
            val level = java.util.logging.Level.ALL
            val formatter = object : Formatter() {
                @Synchronized
                override fun format(record: LogRecord?) = record?.message + "\n"
            }
            Logger.useParentHandlers = false
            val handler = ConsoleHandler()
            handler.formatter = formatter;
            handler.level = level
            Logger.addHandler(handler)
            Logger.level = level
        }
    }

    override fun log(tag: String, msg: String, level: Level) {
        val log = "$tag --> $msg"
        when (level) {
            Level.VERBOSE -> Logger.log(java.util.logging.Level.FINE, log)
            Level.DEBUG -> Logger.log(java.util.logging.Level.CONFIG, log)
            Level.INFO -> Logger.log(java.util.logging.Level.INFO, log)
            Level.WARN -> Logger.log(java.util.logging.Level.WARNING, log)
            Level.ERROR -> Logger.log(java.util.logging.Level.SEVERE, log)
            Level.NOTHING -> Unit
        }
    }
}
