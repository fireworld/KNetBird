package cc.colorcat.netbird

/**
 * Created by cxx on 18-1-22.
 * xx.ch@outlook.com
 */
class NetworkData<out T> private constructor(val code: Int, val msg: String, val data: T?) {
    val isSuccess
        get() = data != null

    companion object {
        fun <R> newSuccess(data: R): NetworkData<R> = NetworkData(200, "ok", data)

        fun <R> newFailure(code: Int, msg: String) = NetworkData<R>(code, msg, null)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NetworkData<*>

        if (code != other.code) return false
        if (msg != other.msg) return false
        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + msg.hashCode()
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "NetworkData(code=$code, msg='$msg', data=$data)"
    }
}
