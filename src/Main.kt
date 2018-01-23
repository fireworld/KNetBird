import cc.colorcat.knetbird.*
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by cxx on 2018/1/17.
 * xx.ch@outlook.com
 */

fun <T> List<T>.toImmutableList(): List<T> {
    return Collections.unmodifiableList(ArrayList(this))
}

fun main(args: Array<String>) {
    val log = Logger.getLogger("KNetBird")
    val url = "http://www.baidu.com"
    val netBird = KNetBird.Builder(url).enableGzip(true).build()
    val qq = "https://dldir1.qq.com/invc/tt/QQBrowser_Setup_9.7.12672.400.exe"
    val savePath = "/home/cxx/qq.exe"
    val fileReq = MRequest.Builder(FileParser.create(savePath))
            .url(qq)
            .downloadListener(object : DownloadListener {
                override fun onChanged(finished: Long, total: Long, percent: Int) {
                    log.log(Level.SEVERE, "download progress, finished=$finished, total=$total, percent=$percent")
                }
            })
            .listener(object : MRequest.Listener<File> {
                override fun onStart() {
                    log.log(Level.SEVERE, "onStart")
                }

                override fun onSuccess(result: File) {
                    log.log(Level.SEVERE, "download success, save path=$result")
                }

                override fun onFailure(code: Int, msg: String) {
                    log.log(Level.SEVERE, "download failure, code=$code, msg=$msg")
                }

                override fun onFinish() {
                    log.log(Level.SEVERE, "onFinish")
                }
            })
            .build()
    val stringReq = MRequest.Builder(StringParser.create("gbk"))
            .url("http://www.pconline.com.cn")
            .listener(object : MRequest.Listener<String> {
                override fun onStart() {
                }

                override fun onSuccess(result: String) {
                    println(result)
                }

                override fun onFailure(code: Int, msg: String) {
                }

                override fun onFinish() {
                }
            })
            .build()
//    netBird.send(stringReq)
    netBird.send(fileReq)
}
