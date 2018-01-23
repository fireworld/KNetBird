package cc.colorcat.knetbird

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
enum class Level(val priority: Int) {
    VERBOSE(2), DEBUG(3), INFO(4), WARN(5), ERROR(6), NOTHING(10)
}
