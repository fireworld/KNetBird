package cc.colorcat.netbird.platform

/**
 * Created by cxx on 2018/1/19.
 * xx.ch@outlook.com
 */
class GenericScheduler : Scheduler {
    override fun onTargetThread(runnable: Runnable) = runnable.run()

    override fun isTargetThread() = true
}
