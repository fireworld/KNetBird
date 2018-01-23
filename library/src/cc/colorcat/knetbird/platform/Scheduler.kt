package cc.colorcat.knetbird.platform

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
interface Scheduler {
    fun onTargetThread(runnable: Runnable)

    fun isTargetThread(): Boolean
}
