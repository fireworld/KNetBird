package cc.colorcat.netbird

import cc.colorcat.netbird.internal.mutableHeadersOf
import java.io.Closeable

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
class Response private constructor(builder: Builder) : Closeable {
    val code = builder.code()
    val msg = builder.msg()
    val headers = builder.headers()
    val responseBody = builder.responseBody()

    fun header(name: String) = headers.value(name)

    fun header(name: String, defaultValue: String) = headers.value(name, defaultValue)

    fun newBuilder() = Builder(this)

    override fun close() {
        responseBody?.close()
    }

    class Builder {
        private var code: Int
        private var msg: String
        private val headers: MutableHeaders
        private var responseBody: ResponseBody?

        constructor() {
            code = HttpStatus.CODE_CONNECT_ERROR
            msg = HttpStatus.MSG_CONNECT_ERROR
            headers = mutableHeadersOf(16)
            responseBody = null
        }

        internal constructor(response: Response) {
            this.code = response.code
            this.msg = response.msg
            this.headers = response.headers.toMutableHeaders()
            this.responseBody = response.responseBody
        }

        fun code() = code

        fun msg() = msg

        fun headers() = headers.toHeaders()

        fun responseBody() = responseBody

        fun code(code: Int): Builder {
            this.code = code
            return this
        }

        fun msg(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun replaceHeaders(headers: Headers): Builder {
            this.headers.clear()
            this.headers.addAll(headers.names(), headers.values())
            return this
        }

        fun addHeader(headers: Headers): Builder {
            this.headers.addAll(headers.names(), headers.values())
            return this
        }

        fun addHeader(name: String, value: String): Builder {
            headers.add(name, value)
            return this
        }

        fun addHeaderIfNot(name: String, value: String): Builder {
            headers.addIfNot(name, value)
            return this
        }

        fun addAllHeader(names: List<String>, values: List<String>): Builder {
            headers.addAll(names, values)
            return this
        }

        fun setHeader(name: String, value: String): Builder {
            headers.set(name, value)
            return this
        }

        fun removeHeader(name: String): Builder {
            headers.removeAll(name)
            return this
        }

        fun clearHeaders(): Builder {
            headers.clear()
            return this
        }

        fun responseBody(responseBody: ResponseBody): Builder {
            this.responseBody = responseBody
            return this
        }

        fun build() = Response(this)
    }
}