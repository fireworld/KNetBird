package cc.colorcat.netbird

import cc.colorcat.netbird.internal.isTargetThread
import cc.colorcat.netbird.internal.onTargetThread

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
internal class MUploadListener(private val listener: UploadListener) : UploadListener {
    companion object {
        internal fun wrap(listener: UploadListener?): UploadListener? = listener?.let { MUploadListener(listener) }
    }

    override fun onChanged(finished: Long, total: Long, percent: Int) {
        if (isTargetThread()) {
            listener.onChanged(finished, total, percent)
        } else {
            onTargetThread(Runnable { listener.onChanged(finished, total, percent) })
        }
    }
}
