package cc.colorcat.knetbird.android

import cc.colorcat.knetbird.Connection
import cc.colorcat.knetbird.platform.Logger
import cc.colorcat.knetbird.platform.Platform
import cc.colorcat.knetbird.platform.Scheduler

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class AndroidPlatform : Platform() {
    override val connection: Connection = AndroidHttpConnection()

    override val scheduler: Scheduler = AndroidScheduler()

    override val logger: Logger = AndroidLogger()
}