package cc.colorcat.knetbird.android

import android.os.Handler
import android.os.Looper
import cc.colorcat.knetbird.platform.Scheduler

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class AndroidScheduler : Scheduler {
    override fun onTargetThread(runnable: Runnable) {
        AndroidScheduler.handler.post(runnable)
    }

    override fun isTargetThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

    private companion object {
        private val handler = Handler(Looper.getMainLooper())
    }
}
