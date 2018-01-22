package cc.colorcat.netbird

import cc.colorcat.netbird.internal.isTargetThread
import cc.colorcat.netbird.internal.onTargetThread

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
internal class MDownloadListener(private val listener: DownloadListener) : DownloadListener {
    companion object {
        internal fun wrap(listener: DownloadListener?): DownloadListener? = listener?.let { MDownloadListener(listener) }
    }

    override fun onChanged(finished: Long, total: Long, percent: Int) {
        if (isTargetThread()) {
            listener.onChanged(finished, total, percent)
        } else {
            onTargetThread(Runnable { listener.onChanged(finished, total, percent) })
        }
    }
}
