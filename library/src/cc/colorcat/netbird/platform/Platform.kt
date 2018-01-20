package cc.colorcat.netbird.platform

import cc.colorcat.netbird.Connection

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
abstract class Platform {
    companion object {
        @Volatile
        private var platform: Platform? = null

        internal fun get(): Platform {
            if (platform == null) {
                synchronized(Platform::class) {
                    if (platform == null) {
                        platform = findPlatform()
                    }
                }
            }
            return platform as Platform
        }

        internal fun findPlatform(): Platform {
            return GenericPlatform()
        }
    }

    abstract fun connection(): Connection

    abstract fun scheduler(): Scheduler

    abstract fun logger(): Logger
}