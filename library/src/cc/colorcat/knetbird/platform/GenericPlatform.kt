package cc.colorcat.knetbird.platform

import cc.colorcat.knetbird.Connection

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class GenericPlatform : Platform() {
    private val connection = HttpConnection()
    private val scheduler = GenericScheduler()
    private val logger = GenericLogger()

    override fun connection(): Connection = connection

    override fun scheduler(): Scheduler = scheduler

    override fun logger(): Logger = logger
}