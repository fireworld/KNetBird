package cc.colorcat.knetbird.platform

import cc.colorcat.knetbird.Connection

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
abstract class Platform {
    companion object {
        @Volatile
        internal var Instance: Platform? = null

        fun get(): Platform {
            if (Platform.Instance == null) {
                synchronized(Platform::class) {
                    if (Platform.Instance == null) {
                        Platform.Instance = findPlatform()
                    }
                }
            }
            return Platform.Instance as Platform
        }

        internal fun findPlatform(): Platform = try {
            val clazz = Class.forName("cc.colorcat.knetbird.android.AndroidPlatform")
            clazz.newInstance() as Platform
        } catch (ignore: Exception) {
            GenericPlatform()
        }
    }

    abstract val connection: Connection

    abstract val scheduler: Scheduler

    abstract val logger: Logger
}
