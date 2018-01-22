package cc.colorcat.knetbird

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
class HttpStatus private constructor() {
    companion object {
        const val CODE_CONNECT_ERROR = -100
        const val MSG_CONNECT_ERROR = "connect error"

        const val CODE_DUPLICATE_REQUEST = -101
        const val MSG_DUPLICATE_REQUEST = "duplicate request"

        const val CODE_REQUEST_CANCELED = -102
        const val MSG_REQUEST_CANCELED = "request canceled"
    }
}