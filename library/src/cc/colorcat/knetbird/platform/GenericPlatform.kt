package cc.colorcat.knetbird.platform

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class GenericPlatform : Platform() {
    override val connection = HttpConnection()

    override val scheduler = GenericScheduler()

    override val logger = GenericLogger()
}
