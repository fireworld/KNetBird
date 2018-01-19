package cc.colorcat.netbird.platform

import cc.colorcat.netbird.KNetBird
import cc.colorcat.netbird.Level
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class GenericLogger : Logger {

    companion object {
        private val logger = java.util.logging.Logger.getLogger(KNetBird::class.simpleName)

        init {
            val level = java.util.logging.Level.ALL
            val formatter = object : Formatter() {
                @Synchronized
                override fun format(record: LogRecord?) = record?.message + "\n"
            }
            logger.useParentHandlers = false
            val handler = ConsoleHandler()
            handler.formatter = formatter;
            handler.level = level
            logger.addHandler(handler)
            logger.level = level
        }
    }

    override fun log(tag: String, msg: String, level: Level) {
        val log = "$tag --> $msg"
        when (level) {
            Level.VERBOSE -> logger.log(java.util.logging.Level.FINE, log)
            Level.DEBUG -> logger.log(java.util.logging.Level.CONFIG, log)
            Level.INFO -> logger.log(java.util.logging.Level.INFO, log)
            Level.WARN -> logger.log(java.util.logging.Level.WARNING, log)
            Level.ERROR -> logger.log(java.util.logging.Level.SEVERE, log)
            Level.NOTHING -> Unit
        }
    }
}
