package cc.colorcat.knetbird

import cc.colorcat.knetbird.internal.mutableHeadersOf
import java.io.Closeable

/**
 * Created by cxx on 2018/1/18.
 * xx.ch@outlook.com
 */
class Response private constructor(builder: Builder) : Closeable {
    val code: Int = builder.code
    val msg: String = builder.msg
    val headers: Headers = builder.headers
    val responseBody: ResponseBody? = builder.responseBody

    fun header(name: String) = headers.value(name)

    fun header(name: String, defaultValue: String) = headers.value(name, defaultValue)

    fun newBuilder() = Builder(this)

    override fun close() {
        responseBody?.close()
    }

    class Builder {
        var code: Int
            private set
        var msg: String
            private set
        private val _headers: MutableHeaders
        val headers
            get() = _headers.toHeaders()
        var responseBody: ResponseBody?
            private set

        constructor() {
            code = HttpStatus.CODE_CONNECT_ERROR
            msg = HttpStatus.MSG_CONNECT_ERROR
            _headers = mutableHeadersOf(16)
            responseBody = null
        }

        internal constructor(response: Response) {
            this.code = response.code
            this.msg = response.msg
            this._headers = response.headers.toMutableHeaders()
            this.responseBody = response.responseBody
        }

        fun code(code: Int): Builder {
            this.code = code
            return this
        }

        fun msg(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun replaceHeaders(headers: Headers): Builder {
            this._headers.clear()
            this._headers.addAll(headers.names(), headers.values())
            return this
        }

        fun addHeader(headers: Headers): Builder {
            this._headers.addAll(headers.names(), headers.values())
            return this
        }

        fun addHeader(name: String, value: String): Builder {
            _headers.add(name, value)
            return this
        }

        fun addHeaderIfNot(name: String, value: String): Builder {
            _headers.addIfNot(name, value)
            return this
        }

        fun addAllHeader(names: List<String>, values: List<String>): Builder {
            _headers.addAll(names, values)
            return this
        }

        fun setHeader(name: String, value: String): Builder {
            _headers.set(name, value)
            return this
        }

        fun removeHeader(name: String): Builder {
            _headers.removeAll(name)
            return this
        }

        fun clearHeaders(): Builder {
            _headers.clear()
            return this
        }

        fun responseBody(responseBody: ResponseBody?): Builder {
            this.responseBody = responseBody
            return this
        }

        fun build() = Response(this)
    }
}
