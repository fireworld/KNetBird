import cc.colorcat.netbird.*
import java.io.IOException
import java.net.URI
import java.nio.charset.Charset
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
    val netBird = KNetBird.Builder(url).build()
    val request = Request.Builder()
            .url("http://www.pconline.com.cn")
            .get()
            .build()
    netBird.newCall(request).enqueue(object : Callback {
        override fun onStart() {
            log.log(Level.SEVERE, "onStart")
            log.log(Level.SEVERE, "------------------")
        }

        override fun onResponse(call: Call, response: Response) {
            for ((name, value) in response.headers) {
                log.log(Level.SEVERE, "$name = $value")
            }
            log.log(Level.SEVERE, "---------------------------")
            log.log(Level.SEVERE, response.responseBody?.string(Charset.forName("GBK")))
        }

        override fun onFailure(call: Call, e: IOException) {
            log.log(Level.SEVERE, "onFailure")
        }

        override fun onFinish() {
            log.log(Level.SEVERE, "onFinish")
        }

    })
}
