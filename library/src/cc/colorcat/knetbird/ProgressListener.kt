package cc.colorcat.knetbird

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */
interface ProgressListener {
    fun onChanged(finished: Long, total: Long, percent: Int)
}

interface DownloadListener : ProgressListener

interface UploadListener : ProgressListener