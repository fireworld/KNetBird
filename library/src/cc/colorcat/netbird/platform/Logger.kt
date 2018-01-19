package cc.colorcat.netbird.platform

import cc.colorcat.netbird.Level

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Logger {
    fun log(tag: String, msg: String, level: Level = Level.DEBUG)
}